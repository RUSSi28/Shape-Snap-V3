package com.orukunnn.shapesnapapp

import android.app.Application
import com.orukunnn.shapesnapapp.di.appModule
import com.orukunnn.shapesnapapp.di.platformAndroidModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(appModule(), platformAndroidModule())
        }
    }
}
