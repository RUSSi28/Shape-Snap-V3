package com.orukunnn.shapesnapapp.data.datasource

import com.orukunnn.shapesnapapp.core.util.AppLogger
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.model.preset.PresetEntity
import com.orukunnn.shapesnapapp.data.model.preset.toPreset
import com.orukunnn.shapesnapapp.data.model.user.UserEntity
import com.orukunnn.shapesnapapp.data.model.user.UserPost
import com.orukunnn.shapesnapapp.data.model.user.UserProfile
import com.orukunnn.shapesnapapp.data.model.user.toProfile
import com.orukunnn.shapesnapapp.data.repository.preset.PresetPageCursor
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.app
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.FieldValue
import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import dev.gitlive.firebase.firestore.FirestoreExceptionCode
import dev.gitlive.firebase.firestore.code
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen

class FirestoreDatasourceImpl : FirestoreDatasource {

    private val firestore get() = Firebase.firestore(Firebase.app, DATABASE_ID)

    /**
     * Android の [kotlinx.coroutines.tasks.await] などは失敗時に ExecutionException でラップするため、
     * 直接 [FirebaseFirestoreException] として catch できないことがある。
     */
    private fun Throwable.findFirestoreException(): FirebaseFirestoreException? {
        val seen = mutableSetOf<Throwable>()
        var current: Throwable? = this
        while (current != null && current !in seen) {
            seen.add(current)
            if (current is FirebaseFirestoreException) return current
            current = current.cause
        }
        return null
    }

    private fun Throwable.isFirestoreUnavailable(): Boolean =
        findFirestoreException()?.code == FirestoreExceptionCode.UNAVAILABLE

    /**
     * 起動直後など、ネットワークが有効でも Firestore が一時的に UNAVAILABLE（オフライン相当）になることがある。
     */
    private suspend fun <T> withFirestoreRetry(block: suspend () -> T): T {
        var delayMs = 400L
        repeat(5) { attempt ->
            try {
                return block()
            } catch (e: Throwable) {
                if (e is CancellationException) throw e
                if (!e.isFirestoreUnavailable() || attempt == 4) throw e
                delay(delayMs)
                delayMs = (delayMs * 2).coerceAtMost(5000)
            }
        }
        error("unreachable")
    }

    /**
     * [runCatching] は非 suspend ラムダのため、その中で Firestore の suspend API を呼ぶと
     * サスペンドが壊れ、以降の処理が実行されない／デバッグでブレークできないことがある。
     */
    private suspend fun <T> suspendRunCatching(block: suspend () -> T): Result<T> =
        try {
            Result.success(block())
        } catch (e: Throwable) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }

    override fun observePresets(): Flow<List<Preset>> =
        firestore.collection(COL_PRESETS).snapshots
            .retryWhen { cause, attempt ->
                if (cause.isFirestoreUnavailable() && attempt < 5L) {
                    delay(400L * (attempt + 1))
                    true
                } else {
                    false
                }
            }
            .map { snapshot ->
                snapshot.documents.mapNotNull { doc ->
                    runCatching {
                        val entity = doc.data(PresetEntity.serializer())
                        entity.toPreset(doc.id)
                    }.getOrElse {
                        AppLogger.w("プリセットのデシリアライズに失敗: ${doc.id}", it)
                        null
                    }
                }
            }.catch { e ->
                AppLogger.e("presets の購読に失敗", e)
                emit(emptyList())
            }

    override suspend fun fetchPresetsPage(
        pageSize: Int,
        cursor: PresetPageCursor?,
    ): Result<Pair<List<Preset>, PresetPageCursor?>> =
        suspendRunCatching {
            withFirestoreRetry {
                // 注意: `__name__`(ドキュメント ID) の DESC は自動インデックスの対象外で
                // FAILED_PRECONDITION になるため、単一フィールドの index が自動作成される
                // `createdAtEpochSeconds` の DESC でページングする。
                val base =
                    firestore
                        .collection(COL_PRESETS)
                        .orderBy(FIELD_CREATED_AT_EPOCH_SECONDS, Direction.DESCENDING)
                        .limit(pageSize.toLong())
                val query =
                    if (cursor != null) {
                        base.startAfter(cursor.snapshot)
                    } else {
                        base
                    }
                val snapshot = query.get()
                val presets =
                    snapshot.documents.mapNotNull { doc ->
                        runCatching {
                            val entity = doc.data(PresetEntity.serializer())
                            entity.toPreset(doc.id)
                        }.getOrNull()
                    }
                val nextCursor =
                    if (presets.size == pageSize) {
                        snapshot.documents.lastOrNull()?.let { PresetPageCursor(it) }
                    } else {
                        null
                    }
                Pair(presets, nextCursor)
            }
        }


    override fun observeUser(uid: String): Flow<UserProfile?> =
        firestore
            .collection(COL_USERS)
            .document(uid)
            .snapshots
            .retryWhen { cause, attempt ->
                if (cause.isFirestoreUnavailable() && attempt < 5L) {
                    delay(400L * (attempt + 1))
                    true
                } else {
                    false
                }
            }
            .map { snapshot ->
                if (!snapshot.exists) {
                    null
                } else {
                    runCatching {
                        val entity = snapshot.data(UserEntity.serializer())
                        entity.toProfile(uid)
                    }.getOrNull()
                }
            }.catch { e ->
                AppLogger.e("user の購読に失敗 uid=$uid", e)
                emit(null)
            }

    override suspend fun ensureUserDocument(
        uid: String,
        displayName: String?,
        photoUrl: String?,
    ): Result<Unit> =
        suspendRunCatching {
            withFirestoreRetry {
                val ref = firestore.collection(COL_USERS).document(uid)
                val snap = ref.get()
                if (!snap.exists) {
                    ref.set(
                        UserEntity.serializer(),
                        UserEntity(displayName = displayName, photoUrl = photoUrl),
                        merge = true,
                    )
                }
            }
        }.onFailure { AppLogger.w("ensureUserDocument failed uid=$uid", it) }

    override suspend fun togglePresetLike(presetId: String, uid: String): Result<Unit> =
        suspendRunCatching {
            val ref = firestore.collection(COL_PRESETS).document(presetId)
            val snapshot = ref.get()
            if (!snapshot.exists) error("preset not found")
            val entity = snapshot.data(PresetEntity.serializer())
            val likeUpdate =
                if (uid in entity.likedUserIds) {
                    FieldValue.arrayRemove(uid)
                } else {
                    FieldValue.arrayUnion(uid)
                }
            ref.updateFields { (FIELD_LIKED_USER_IDS to likeUpdate) }
        }

    override suspend fun addPresetToUserStorage(uid: String, presetId: String): Result<Unit> =
        suspendRunCatching {
            val userRef = firestore.collection(COL_USERS).document(uid)
            val presetRef = firestore.collection(COL_PRESETS).document(presetId)
            val userSnap = userRef.get()
            val userEntity =
                if (userSnap.exists) {
                    userSnap.data(UserEntity.serializer())
                } else {
                    UserEntity()
                }
            val storage = userEntity.storage
            if (presetId in storage) return@suspendRunCatching
            val newStorage = storage + presetId
            userRef.set(
                UserEntity.serializer(),
                userEntity.copy(storage = newStorage),
                merge = true
            )
            val presetSnap = presetRef.get()
            if (!presetSnap.exists) error("preset not found")
            val presetEntity = presetSnap.data(PresetEntity.serializer())
            val newSaved =
                if (uid in presetEntity.savedUserIds) {
                    presetEntity.savedUserIds
                } else {
                    presetEntity.savedUserIds + uid
                }
            presetRef.set(
                PresetEntity.serializer(),
                presetEntity.copy(savedUserIds = newSaved),
                merge = true
            )
        }

    override suspend fun removePresetFromUserStorage(uid: String, presetId: String): Result<Unit> =
        suspendRunCatching {
            val userRef = firestore.collection(COL_USERS).document(uid)
            val presetRef = firestore.collection(COL_PRESETS).document(presetId)
            val userSnap = userRef.get()
            if (!userSnap.exists) return@suspendRunCatching
            val userEntity = userSnap.data(UserEntity.serializer())
            val newStorage = userEntity.storage - presetId
            userRef.set(
                UserEntity.serializer(),
                userEntity.copy(storage = newStorage),
                merge = true
            )
            val presetSnap = presetRef.get()
            if (!presetSnap.exists) return@suspendRunCatching
            val presetEntity = presetSnap.data(PresetEntity.serializer())
            val newSaved = presetEntity.savedUserIds - uid
            presetRef.set(
                PresetEntity.serializer(),
                presetEntity.copy(savedUserIds = newSaved),
                merge = true
            )
        }

    override suspend fun deleteUserPost(uid: String, presetId: String): Result<Unit> =
        suspendRunCatching {
            withFirestoreRetry {
                val presetRef = firestore.collection(COL_PRESETS).document(presetId)
                presetRef.delete()

                val userRef = firestore.collection(COL_USERS).document(uid)
                val userSnapshot = userRef.get()
                if (!userSnapshot.exists) return@withFirestoreRetry

                val userEntity = userSnapshot.data(UserEntity.serializer())
                userRef.set(
                    UserEntity.serializer(),
                    userEntity.copy(posts = userEntity.posts - presetId),
                    merge = true,
                )
            }
        }.onFailure {
            AppLogger.w("投稿の削除に失敗 uid=$uid presetId=$presetId", it)
        }

    override suspend fun fetchUserPosts(
        uid: String,
        pageSize: Int,
        startAfterDocumentId: String?,
    ): Result<UserPostsPage> =
        suspendRunCatching {
            withFirestoreRetry {
                val base =
                    firestore
                        .collection(COL_USERS)
                        .document(uid)
                        .collection(COL_POSTS)
                        .orderBy(FIELD_CREATED_AT, Direction.DESCENDING)
                        .limit(pageSize.toLong())

                val query =
                    if (startAfterDocumentId != null) {
                        val cursor =
                            firestore
                                .collection(COL_USERS)
                                .document(uid)
                                .collection(COL_POSTS)
                                .document(startAfterDocumentId)
                                .get()
                        base.startAfter(cursor)
                    } else {
                        base
                    }

                val snapshot = query.get()
                val items =
                    snapshot.documents.mapNotNull { doc ->
                        runCatching {
                            val post = doc.data(UserPost.serializer())
                            post.copy(id = doc.id)
                        }.getOrNull()
                    }
                val next = items.lastOrNull()?.id?.takeIf { items.size == pageSize }
                UserPostsPage(items = items, nextCursor = next)
            }
        }

    companion object {
        private const val DATABASE_ID = "shape-snap"

        private const val COL_PRESETS = "presets"
        private const val COL_USERS = "users"
        private const val COL_POSTS = "posts"
        private const val FIELD_LIKED_USER_IDS = "likedUserIds"
        private const val FIELD_CREATED_AT = "createdAt"
        private const val FIELD_CREATED_AT_EPOCH_SECONDS = "createdAtEpochSeconds"
    }
}
