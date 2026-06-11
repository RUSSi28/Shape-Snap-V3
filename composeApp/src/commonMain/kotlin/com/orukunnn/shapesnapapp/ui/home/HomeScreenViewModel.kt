package com.orukunnn.shapesnapapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.core.platform.CredentialProvider
import com.orukunnn.shapesnapapp.core.util.AppLogger
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.model.user.UserProfile
import com.orukunnn.shapesnapapp.data.model.user.mergeWithFirestore
import com.orukunnn.shapesnapapp.data.repository.auth.AuthRepository
import com.orukunnn.shapesnapapp.data.repository.preset.PresetRepository
import com.orukunnn.shapesnapapp.data.repository.user.UserRepository
import com.orukunnn.shapesnapapp.ui.common.LoadState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val presets: ImmutableList<Preset>,
        val hasMore: Boolean,
    ) : HomeUiState

    data class Error(
        val message: String,
    ) : HomeUiState
}

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenViewModel(
    private val presetRepository: PresetRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val credentialProvider: CredentialProvider,
) : ViewModel() {
    val homeState: StateFlow<HomeUiState> =
        presetRepository.presets
            .map { presets -> presets.toHomeUiState() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = presetRepository.presets.value.toHomeUiState(),
            )

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _showLimitReachedDialog = MutableStateFlow(false)
    val showLimitReachedDialog: StateFlow<Boolean> = _showLimitReachedDialog.asStateFlow()

    private val authUser = authRepository.currentUser

    val currentUser: StateFlow<UserProfile?> =
        authUser
            .flatMapLatest { auth ->
                if (auth == null) {
                    flowOf(null)
                } else {
                    userRepository.observeUser(auth.uid).map { firestore ->
                        auth.mergeWithFirestore(firestore)
                    }
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = authRepository.currentUser.value,
            )

    private val _signInState = MutableStateFlow<LoadState<Unit>>(LoadState.Idle)
    val signInState: StateFlow<LoadState<Unit>> = _signInState.asStateFlow()

    init {
        viewModelScope.launch {
            authUser
                .filterNotNull()
                .catch { AppLogger.e("authUser collect failed", it) }
                .collect { auth ->
                    userRepository
                        .ensureUserDocument(auth.uid, auth.displayName, auth.photoUrl)
                        .onFailure { AppLogger.w("ensureUserDocument skipped", it) }
                }
        }
    }

    fun loadMore() {
        // スナップショットで一覧をまとめて受け取るため追加ページはなし
    }

    fun refreshPresets() {
        viewModelScope.launch {
            _isRefreshing.value = true
            delay(300)
            _isRefreshing.value = false
        }
    }

    fun toggleLike(presetId: String) {
        viewModelScope.launch {
            val uid = authRepository.currentUser.filterNotNull().first().uid
            userRepository.togglePresetLike(presetId, uid)
        }
    }

    fun saveToStorage(presetId: String) {
        viewModelScope.launch {
            val uid = authRepository.currentUser.filterNotNull().first().uid
            val user = currentUser.value ?: return@launch
            if (presetId in user.storage) return@launch
            if (user.storage.size >= FREE_LIMIT) {
                _showLimitReachedDialog.value = true
                return@launch
            }
            userRepository.addPresetToStorage(uid, presetId)
        }
    }

    fun dismissLimitDialog() {
        _showLimitReachedDialog.value = false
    }

    fun signInWithGoogle() {
        viewModelScope.launch {
            _signInState.value = LoadState.Loading
            val tokensResult = credentialProvider.getGoogleTokens()
            if (tokensResult.isFailure) {
                val e = tokensResult.exceptionOrNull() ?: Throwable("token error")
                _signInState.value = LoadState.Error(e.message ?: "error")
                return@launch
            }
            val tokens = tokensResult.getOrThrow()
            val signIn =
                authRepository.signInWithGoogleCredentials(
                    idToken = tokens.idToken,
                    accessToken = tokens.accessToken,
                )
            _signInState.value =
                if (signIn.isSuccess) {
                    LoadState.Success(Unit)
                } else {
                    LoadState.Error(signIn.exceptionOrNull()?.message ?: "error")
                }
        }
    }

    companion object {
        const val FREE_LIMIT: Int = 5

        private fun List<Preset>?.toHomeUiState(): HomeUiState =
            when (this) {
                null ->
                    HomeUiState.Loading
                else ->
                    HomeUiState.Success(
                        presets = sortedByDescending { it.id }.toPersistentList(),
                        hasMore = false,
                    )
            }
    }
}
