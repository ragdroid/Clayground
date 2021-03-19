package com.ragdroid.clayground.shared

import co.touchlab.kermit.Kermit
import co.touchlab.stately.ensureNeverFrozen
import com.ragdroid.clayground.shared.ui.base.GenericNativeViewModel
import com.ragdroid.clayground.shared.ui.base.MviViewModel
import com.ragdroid.clayground.shared.ui.moviedetail.MovieDetailEvent
import com.ragdroid.clayground.shared.ui.moviedetail.MovieDetailState
import com.ragdroid.clayground.shared.ui.moviedetail.MovieDetailViewEffect
import com.ragdroid.clayground.shared.ui.moviedetail.MovieDetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@FlowPreview
class NativeViewModel<State, Event, ViewEffect>(
    private val viewModel: GenericNativeViewModel<State, Event, ViewEffect>,
    private val nativeCallback: NativeCallback<State, Event, ViewEffect>,
    private val kermit: Kermit
) {
    private val mainScope = MainScope(Dispatchers.Main, kermit)
    init {
        ensureNeverFrozen()
        initLoop()
    }
    fun dispatchEvent(event: Event) {
        mainScope.launch {
            viewModel.dispatchEvent(event)
        }
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
        mainScope.onDestroy()
    }
}

abstract class NativeCallback<S, E, VF> {
    fun render(state: S) {}
    fun handleViewEffects(viewEffect: VF) {}
}
