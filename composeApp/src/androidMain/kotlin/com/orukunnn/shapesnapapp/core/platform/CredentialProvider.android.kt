package com.orukunnn.shapesnapapp.core.platform

import android.content.Context
import android.os.CancellationSignal
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialManagerCallback
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AndroidCredentialProvider(
    private val applicationContext: Context,
    private val webClientId: String,
) : CredentialProvider {

    private val credentialManager by lazy { CredentialManager.create(applicationContext) }
    private val executor by lazy { Executors.newSingleThreadExecutor() }

    override suspend fun getGoogleTokens(): Result<GoogleAuthTokens> =
        withContext(Dispatchers.Main) {
            if (webClientId.isBlank()) {
                return@withContext Result.failure(
                    IllegalStateException(
                        "GOOGLE_WEB_CLIENT_ID が未設定です。",
                    ),
                )
            }
            runCatching {
                val googleIdOption =
                    GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId(webClientId)
                        .build()
                val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
                val response =
                    suspendCancellableCoroutine { continuation ->
                        credentialManager.getCredentialAsync(
                            applicationContext,
                            request,
                            CancellationSignal(),
                            executor,
                            object :
                                CredentialManagerCallback<GetCredentialResponse, GetCredentialException> {
                                override fun onResult(result: GetCredentialResponse) {
                                    continuation.resume(result)
                                }

                                override fun onError(e: GetCredentialException) {
                                    continuation.resumeWithException(e)
                                }
                            },
                        )
                    }
                val credential = response.credential
                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleId = GoogleIdTokenCredential.createFrom(credential.data)
                        // Android の CredentialManager フローでは accessToken は提供されないので null。
                        GoogleAuthTokens(idToken = googleId.idToken, accessToken = null)
                    } catch (e: GoogleIdTokenParsingException) {
                        throw e
                    }
                } else {
                    error("Google ID トークン以外のクレデンシャルが返されました")
                }
            }
        }
}