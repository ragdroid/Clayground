package com.ragdroid.clayground.shared

import co.touchlab.kermit.Kermit
import co.touchlab.stately.ensureNeverFrozen
import com.ragdroid.clayground.shared.ui.base.GenericNativeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@FlowPreview
class NativeViewModel<State, Event, ViewEffect>(
    private val viewModel: GenericNativeViewModel<State, Event, ViewEffect>,
    private val nativeCallback: NativeCallback<State, Event, ViewEffect>
) {
    val kermit = kermitLogger()
    private val mainScope = MainScope(Dispatchers.Main, kermit)
    init {
        ensureNeverFrozen()
        initLoop()
    }
    fun dispatchEvent(event: Event) {
        kermit.d("inside dispatch Event with $event", "NativeViewModel")
        mainScope.launch {
            kermit.d("inside dispatch Event launch $event", "NativeViewModel")
            viewModel.dispatchEvent(event)
        }
        kermit.d("inside dispatch Event end with $event", "NativeViewModel")
    }

    private fun initLoop() {
        viewModel.initializeIn(mainScope)
        mainScope.launch {
            viewModel.stateFlow.collect {
                nativeCallback.render(it)
            }
            viewModel.uiEffectsFlow.collect {
                nativeCallback.handleViewEffects(it)
            }
        }
    }

    fun onDestroy() {
        kermit.d("onDestroy called", "NativeViewModel")
        mainScope.onDestroy()
    }
}

abstract class NativeCallback<S, E, VF> {
    fun render(state: S) {}
    fun handleViewEffects(viewEffect: VF) {}
}
