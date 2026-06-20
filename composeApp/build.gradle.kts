import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.googleServices)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        // AdMob は GoogleMobileAds の巨大な cinterop を避け、UIKit だけのヘッダー + iosApp の .m 実装で連携する。
        iosTarget.compilations.getByName("main").cinterops.create("shapesnapAds") {
            definitionFile.set(layout.projectDirectory.file("src/nativeInterop/cinterop/shapesnapAds.def"))
            includeDirs(layout.projectDirectory.dir("src/nativeInterop/cinterop"))
        }

        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            // シンボルは iosApp の shapesnap_ads_bridge.m で解決（Pods の Google-Mobile-Ads-SDK とリンク）
            linkerOpts("-undefined", "dynamic_lookup")
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.androidx.credentials)
            implementation(libs.androidx.credentials.play.services)
            implementation(libs.googleid)
            // Coil の Ktor 経由で HttpClient() するためのエンジン（無いとネット画像が読めない）
            implementation(libs.ktor.client.okhttp)
            implementation(libs.play.services.ads)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.uiBackhandler)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.navigation.compose)
            implementation(libs.firebase.app)
            implementation(libs.firebase.auth)
            implementation(libs.firebase.firestore)
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.coroutines)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kermit)
            implementation(libs.tabler)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
        }
    }
}

android {
    namespace = "com.orukunnn.shapesnapapp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.orukunnn.shapesnapapp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        buildConfigField(
            "String",
            "GOOGLE_WEB_CLIENT_ID",
            "\"${project.findProperty("GOOGLE_WEB_CLIENT_ID") ?: ""}\"",
        )
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}