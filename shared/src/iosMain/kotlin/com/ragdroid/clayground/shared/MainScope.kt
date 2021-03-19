package com.ragdroid.clayground.shared

import co.touchlab.kermit.Kermit
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

class MainScope(
    private val mainContext: CoroutineContext,
    private val kermit: Kermit
): CoroutineScope {
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        kermit.d {
            throwable.printStackTrace()
            throwable.message ?: "Unknown Error in Coroutines"
        }
    }
    override val coroutineContext: CoroutineContext
        get() = mainContext + supervisorJob
    private val supervisorJob = SupervisorJob() + exceptionHandler



    fun onDestroy() {
        //TODO cancel children?
        supervisorJob.cancel()
    }
}
