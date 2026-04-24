package com.orukunnn.shapesnapapp.di

import com.orukunnn.shapesnapapp.BuildConfig
import com.orukunnn.shapesnapapp.core.platform.AndroidCredentialProvider
import com.orukunnn.shapesnapapp.core.platform.CredentialProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun platformAndroidModule() =
    module {
        single<CredentialProvider> {
            AndroidCredentialProvider(androidContext(), BuildConfig.GOOGLE_WEB_CLIENT_ID)
        }
    }
