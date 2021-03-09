object AndroidSdk {
    const val minSdk = 21
    const val compileSdk = 30
}

object Compose {
    object Versions {
        const val activity = "1.3.0-alpha03"
    }
    const val activity = "androidx.activity:activity-compose:${Versions.activity}"
}

object DI {
    object Versions {
        const val hilt = "2.28-alpha"
        const val lifecycle = "1.0.0-alpha01"
    }

    const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val lifecycle = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.lifecycle}"
    const val kaptHilt =  "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    const val kaptLifecycle =  "androidx.hilt:hilt-compiler:${Versions.lifecycle}"
}

object Other {
    object Versions {
        const val timber = "4.1.2"
    }
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
}
