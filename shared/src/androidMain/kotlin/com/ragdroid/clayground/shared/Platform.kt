package com.ragdroid.clayground.shared

import co.touchlab.kermit.LogcatLogger
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun kermitLogger(): Logger = LogcatLogger()

actual val MainDispatcher: CoroutineDispatcher = Dispatchers.Main

actual val BackgroundDispatcher: CoroutineDispatcher = Dispatchers.IO
