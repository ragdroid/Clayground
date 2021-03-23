package com.ragdroid.clayground.shared

import co.touchlab.stately.ensureNeverFrozen
import com.ragdroid.clayground.shared.di.SharedModule
import com.ragdroid.clayground.shared.ui.base.GenericNativeViewModel
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
    private val kermit = SharedModule.kermitLogger
    private val mainScope = MainScope()
    init {
        ensureNeverFrozen()
        initLoop()
    }
    fun dispatchEvent(event: Event) {
        kermit.d {
            "inside dispatch Event with $event"
        }
        mainScope.launch {
            kermit.d {
                "inside dispatch Event launch $event isActive $isActive"
            }
            viewModel.dispatchEvent(event)
        }
        kermit.d {
            "inside dispatch Event end with $event"
        }
    }

    private fun initLoop() {
        viewModel.initializeIn(mainScope)
        mainScope.launch {
            viewModel.stateFlow.collect {
                kermit.d {
                    "inside stateFlow collect $it"
                }
                render(it as State)
            }
            viewModel.uiEffectsFlow.collect {
                kermit.d {
                    "inside uiEffectsFLow collect $it"
                }
                viewEffectHandler(it as ViewEffect)
            }
        }
    }

    fun onDestroy() {
        kermit.d {
            "onDestroy called"
        }
        mainScope.cancel()
    }
}
