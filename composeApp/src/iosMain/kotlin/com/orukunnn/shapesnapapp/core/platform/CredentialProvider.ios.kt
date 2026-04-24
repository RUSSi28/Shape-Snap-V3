package com.orukunnn.shapesnapapp.core.platform

import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Swift 側から Google サインインのコールバックを登録するためのブリッジ。
 * iOSApp 起動時に [tokenProvider] を設定する。
 *
 * tokenProvider のシグネチャ:
 *   (onResult: (idToken: String?, errorMessage: String?) -> Unit) -> Unit
 * 実装は非同期で GIDSignIn を呼び出し、完了時に onResult(idToken, null)
 * またはエラー時に onResult(null, errorMessage) を呼ぶ。
 */
object IosCredentialBridge {
    /**
     * Swift 側が設定する Google サインインコールバック。
     * onResult(idToken, accessToken, errorMessage) を 1 回呼び出す。
     */
    var tokenProvider: ((onResult: (String?, String?, String?) -> Unit) -> Unit)? = null
}

/**
 * iOS 実装の [CredentialProvider]。実際のサインインフローは Swift 側で
 * [IosCredentialBridge.tokenProvider] として登録された処理に委譲する。
 */
class IosCredentialProvider : CredentialProvider {
    override suspend fun getGoogleTokens(): Result<GoogleAuthTokens> {
        val provider = IosCredentialBridge.tokenProvider
            ?: return Result.failure(
                IllegalStateException(
                    "IosCredentialBridge.tokenProvider が未登録です。Swift 側でサインイン処理を登録してください。",
                ),
            )
        return suspendCancellableCoroutine { cont ->
            provider { idToken, accessToken, errorMessage ->
                if (idToken != null) {
                    cont.resume(
                        Result.success(
                            GoogleAuthTokens(idToken = idToken, accessToken = accessToken),
                        ),
                    )
                } else {
                    cont.resume(
                        Result.failure(
                            Throwable(errorMessage ?: "Google サインインに失敗しました"),
                        ),
                    )
                }
            }
        }
    }
}
