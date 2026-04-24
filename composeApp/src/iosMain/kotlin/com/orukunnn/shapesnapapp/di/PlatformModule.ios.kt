package com.orukunnn.shapesnapapp.di

import com.orukunnn.shapesnapapp.core.platform.CredentialProvider
import com.orukunnn.shapesnapapp.core.platform.IosCredentialProvider
import org.koin.dsl.module

fun platformIosModule() =
    module {
        single<CredentialProvider> { IosCredentialProvider() }
    }
