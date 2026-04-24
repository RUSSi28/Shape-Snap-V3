package com.orukunnn.shapesnapapp.core.platform

/**
 * Google サインインで得られるトークン群。
 * iOS の Firebase Auth は [accessToken] が必須なのでプラットフォームに応じて設定する。
 */
data class GoogleAuthTokens(
    val idToken: String,
    val accessToken: String?,
)

/**
 * Google サインイン用のトークン取得。プラットフォーム実装は DI で注入する。
 */
interface CredentialProvider {
    suspend fun getGoogleTokens(): Result<GoogleAuthTokens>
}
