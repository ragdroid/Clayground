package com.ragdroid.clayground.shared

import co.touchlab.kermit.Logger
import platform.UIKit.UIDevice
import co.touchlab.kermit.NSLogLogger

actual class Platform actual constructor() {
    actual val platform: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun logger(): Logger = NSLogLogger()
