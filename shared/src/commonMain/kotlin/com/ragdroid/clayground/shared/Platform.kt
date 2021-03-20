package com.ragdroid.clayground.shared

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineDispatcher

expect class Platform() {
    val platform: String
}
expect fun kermitLogger(): Logger

//TODO MainDispatcher and BgDispatcher need not be provided via expect actual, this is
//here just for experimentation purposes
//we have coroutines multithreading support in kotlin native
expect val MainDispatcher: CoroutineDispatcher

expect val BackgroundDispatcher: CoroutineDispatcher
