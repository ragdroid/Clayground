package com.ragdroid.clayground.moviedetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ragdroid.clayground.shared.api.MoviesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val moviesService: MoviesService
): ViewModel() {

    private val eventsFlow = MutableSharedFlow<MovieDetailEvent>(
        replay = 0
    )
    private val sideEffectsFlow = MutableSharedFlow<MovieDetailSideEffect>(
        replay = 0
    )
    private val _uiEffectsFlow = MutableSharedFlow<MovieDetailViewEffect>()
    val uiEffectsFlow: SharedFlow<MovieDetailViewEffect>
        get() = _uiEffectsFlow

    private val _stateFlow = MutableStateFlow<MovieDetailState>(MovieDetailState())
    val stateFlow: StateFlow<MovieDetailState>
        get() = _stateFlow

    init {
        val resultsFlow = sideEffectsFlow
            .flatMapMerge {
                Timber.d("Side Effect: $it")
                val movieDetailSideEffectHandler = MovieDetailSideEffectHandler(moviesService)
                movieDetailSideEffectHandler.process(it, _uiEffectsFlow)
            }
        merge(eventsFlow, resultsFlow)
            .onEach {
                Timber.d("Event: $it")
            }
            .scan(MovieDetailState()) { state, event ->
                val next = MovieDetailUpdate().update(state, event)
                next.effects.forEach {
                    sideEffectsFlow.emit(it)
                }
                if (next.hasModel()) next.safeModel() else state
            }
            .distinctUntilChanged { old, new -> old == new }
            .onEach {
                _stateFlow.value = it
                Timber.d("State $it")
            }
            .launchIn(viewModelScope)
    }

    fun dispatchEvent(event: MovieDetailEvent) {
        viewModelScope.launch {
            eventsFlow.emit(event)
        }
    }

}
