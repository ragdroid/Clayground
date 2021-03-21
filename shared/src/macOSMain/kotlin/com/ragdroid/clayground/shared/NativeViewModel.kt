package com.ragdroid.clayground.shared

import co.touchlab.kermit.Kermit
import co.touchlab.stately.ensureNeverFrozen
import com.ragdroid.clayground.shared.ui.base.GenericNativeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@FlowPreview
class NativeViewModel<State, Event, ViewEffect>(
    private val viewModel: GenericNativeViewModel<State, Event, ViewEffect>,
    private val render: (State) -> Unit,
    private val viewEffectHandler: (ViewEffect) -> Unit
) {
    val kermit = kermitLogger()
    private val mainScope = kotlinx.coroutines.MainScope()
    init {
        ensureNeverFrozen()
        initLoop()
    }
    fun dispatchEvent(event: Event) {
        kermit.d("inside dispatch Event with $event", "NativeViewModel")
        mainScope.launch {
            kermit.d("inside dispatch Event launch $event isActive $isActive", "NativeViewModel")
            viewModel.dispatchEvent(event)
        }
        kermit.d("inside dispatch Event end with $event", "NativeViewModel")
    }

    private fun initLoop() {
        viewModel.initializeIn(mainScope)
        mainScope.launch {
            viewModel.stateFlow.collect {
                kermit.d("inside stateFlow collect $it", "NativeViewModel")
                render(it as State)
            }
            viewModel.uiEffectsFlow.collect {
                kermit.d("inside uiEffectsFLow collect $it", "NativeViewModel")
                viewEffectHandler(it as ViewEffect)
            }
        }
    }

    fun onDestroy() {
        kermit.d("onDestroy called", "NativeViewModel")
        mainScope.cancel()
    }
}
