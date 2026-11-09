import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    )
    android {
        namespace = "com.breckneck.feature.flashcard"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        androidResources {
            enable = true
        }
    }
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.uiTooling)
        }
        commonMain.dependencies {
            implementation(project(":core:domain"))
            implementation(project(":core:ui:designsystem"))
            implementation(project(":core:common"))
            implementation(libs.navigation.compose)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.lifecycle.viewmodel.savedstate)
            implementation(libs.lifecycle.runtime.compose)
            implementation(libs.navigationevent.compose)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}


