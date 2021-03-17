package com.ragdroid.clayground.shared

import co.touchlab.stately.ensureNeverFrozen
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
class MovieDetailsNativeViewModel(
    val viewModel: MovieDetailViewModel,
    val nativeCallback: NativeCallback<MovieDetailState, MovieDetailEvent, MovieDetailViewEffect>
) {
    val mainScope = MainScope(Dispatchers.Main)
    init {
        ensureNeverFrozen()
        initLoop()
    }
    fun dispatchEvent(event: MovieDetailEvent) {
        mainScope.launch {
            viewModel.dispatchEvent(event)
        }
    }

    fun initLoop() {
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

interface NativeCallback<S, E, VF> {
    fun render(state: S)
    fun handleViewEffects(viewEffect: VF)
}
