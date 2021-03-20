package com.ragdroid.clayground.shared

import co.touchlab.kermit.Logger
import platform.UIKit.UIDevice
import co.touchlab.kermit.NSLogLogger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_queue_t
import kotlin.coroutines.CoroutineContext

actual class Platform actual constructor() {
    actual val platform: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun kermitLogger(): Logger = NSLogLogger()
actual val MainDispatcher: CoroutineDispatcher = NsQueueDispatcher(dispatch_get_main_queue())
actual val BackgroundDispatcher: CoroutineDispatcher = MainDispatcher

internal class NsQueueDispatcher(private val dispatchQueue: dispatch_queue_t) : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        dispatch_async(dispatchQueue) {
            block.run()
        }
    }
}
