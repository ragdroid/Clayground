package com.ragdroid.clayground.shared

import co.touchlab.kermit.LogcatLogger
import co.touchlab.kermit.Logger

actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun kermitLogger(): Logger = LogcatLogger()
