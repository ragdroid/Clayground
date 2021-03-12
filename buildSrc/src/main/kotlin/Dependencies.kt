object AndroidSdk {
    const val minSdk = 21
    const val compileSdk = 30
    const val targetSdk = 30
}
object AndroidX {
    object Versions {
        const val lifecycle = "2.3.0"
        const val core = "1.3.2"
        const val appCompat = "1.2.0"
    }
    const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.core}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
}

object Google {
    object Versions {
        const val material = "1.3.0"
    }
    const val material = "com.google.android.material:material:${Versions.material}"
}
object Serialization {
    object Versions {
        const val serialization = "1.0.1"
    }
    const val kotlinx = "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.serialization}"
}

object Ktor {
    object Versions {
        const val ktor = "1.5.2"
    }
    const val core = "io.ktor:ktor-client-core:${Versions.ktor}"
    const val json = "io.ktor:ktor-client-json:${Versions.ktor}"
    const val logging = "io.ktor:ktor-client-logging:${Versions.ktor}"
    const val serialization = "io.ktor:ktor-client-serialization:${Versions.ktor}"

    const val android = "io.ktor:ktor-client-android:${Versions.ktor}"
    const val ios = "io.ktor:ktor-client-ios:${Versions.ktor}"

}

object DI {
    object Versions {
        const val hilt = "2.33-beta"
        const val lifecycle = "1.0.0-alpha01"
        const val koin = "3.0.1-beta-1"
    }

    const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val lifecycle = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.lifecycle}"
    const val kaptHilt =  "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    const val kaptLifecycle =  "androidx.hilt:hilt-compiler:${Versions.lifecycle}"
    const val koinCore = "io.insert-koin:koin-core:${Versions.koin}"
    const val koinTest = "io.insert-koin:koin-test:${Versions.koin}"
}

object Compose {
    object Versions {
        const val compose = "1.0.0-beta01"
        const val activity = "1.3.0-alpha03"

    }
    const val activity = "androidx.activity:activity-compose:${Versions.activity}"
    const val ui = "androidx.compose.ui:ui:${Versions.compose}"
    const val material = "androidx.compose.material:material:${Versions.compose}"
    const val materialIcons = "androidx.compose.material:material-icons-extended:${Versions.compose}"
    const val uiTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    const val composeUiTest = "androidx.compose.ui:ui-test-junit4:${Versions.compose}"
}

object Other {
    object Versions {
        const val timber = "4.1.2"
    }
    const val buildkonfig = "0.7.0"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
}

object Testing {
    object Versions {
        const val junit = "4.13.2"
        const val androidXTestExt = "1.1.2"
    }
    const val junit = "junit:junit:${Versions.junit}"
    const val androidXTestExt = "androidx.test.ext:junit:${Versions.androidXTestExt}"
}
