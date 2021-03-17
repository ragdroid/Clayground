package com.ragdroid.clayground.shared

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class MainScope(
    private val mainContext: CoroutineContext
): CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = mainContext + supervisorJob
    private val supervisorJob = SupervisorJob()

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        //TODO print
    }

    fun onDestroy() {
        //TODO cancel children?
        supervisorJob.cancel()
    }
}
