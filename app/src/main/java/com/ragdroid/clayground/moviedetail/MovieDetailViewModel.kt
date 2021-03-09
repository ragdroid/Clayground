package com.ragdroid.clayground.moviedetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import timber.log.Timber

@FlowPreview
@ExperimentalCoroutinesApi
class MovieDetailViewModel: @ViewModelInject ViewModel() {

    private val eventsFlow: SharedFlow<MovieDetailEvent> = MutableSharedFlow(
        replay = 0
    )
    private val sideEffectsFlow = MutableSharedFlow<MovideDetailSideEffect>(
        replay = 0
    )
    private val _uiEffectsFlow = MutableSharedFlow<MovieDetailState>()
    val uiEffectsFlow: SharedFlow<MovieDetailState>
        get() = _uiEffectsFlow

    private val _stateFlow = MutableStateFlow<MovieDetailState>(MovieDetailState())
    val stateFlow: SharedFlow<MovieDetailState>
    get() = _stateFlow

    init {
        val resultsFlow = sideEffectsFlow
            .flatMapMerge {
                Timber.d("Side Effect: $it")
                it.process(_uiEffectsFlow)
            }
        merge(eventsFlow, resultsFlow)
            .onEach {
                Timber.d("Event: $it")
            }
            .scan(MovieDetailState()) { state, event ->
                    state.update(event, sideEffectsFlow, _uiEffectsFlow)
                }
            .onEach {
                _stateFlow.value = it
                Timber.d("State $it")
            }
            .launchIn(viewModelScope)
    }

}
