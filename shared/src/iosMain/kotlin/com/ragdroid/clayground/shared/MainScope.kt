package com.ragdroid.clayground.shared

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

class MainScope(
    private val mainContext: CoroutineContext,
    private val kermit: Logger
): CoroutineScope {
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        kermit.d("MainScope", throwable.message ?: "Unknown Error")
    }
    override val coroutineContext: CoroutineContext
        get() = mainContext + supervisorJob
    private val supervisorJob = SupervisorJob() + exceptionHandler



    fun onDestroy() {
        //TODO cancel children?
        supervisorJob.cancel()
    }
}
