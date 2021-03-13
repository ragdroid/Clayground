package com.ragdroid.clayground.shared.ui.moviedetail

import com.ragdroid.clayground.shared.ui.base.MviViewModel
import com.ragdroid.clayground.shared.domain.repository.MovieDetailRepository
import kotlinx.coroutines.CoroutineScope
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

@FlowPreview
@ExperimentalCoroutinesApi
class MovieDetailViewModel(
    private val movieDetailRepository: MovieDetailRepository
): MviViewModel<MovieDetailState, MovieDetailViewEffect> {

    private val eventsFlow = MutableSharedFlow<MovieDetailEvent>(
        replay = 0
    )
    private val sideEffectsFlow = MutableSharedFlow<MovieDetailSideEffect>(
        replay = 0
    )
    private val _uiEffectsFlow = MutableSharedFlow<MovieDetailViewEffect>()
    override val uiEffectsFlow: SharedFlow<MovieDetailViewEffect>
        get() = _uiEffectsFlow

    private val _stateFlow = MutableStateFlow<MovieDetailState>(MovieDetailState())
    override val stateFlow: StateFlow<MovieDetailState>
        get() = _stateFlow

    fun initializeIn(viewModelScope: CoroutineScope) {
        val resultsFlow = sideEffectsFlow
            .flatMapMerge {
//                Timber.d("Side Effect: $it")
                val movieDetailSideEffectHandler = MovieDetailSideEffectHandler(movieDetailRepository)
                movieDetailSideEffectHandler.process(it, _uiEffectsFlow)
            }
        merge(eventsFlow, resultsFlow)
            .onEach {
//                Timber.d("Event: $it")
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
//                Timber.d("State $it")
            }.launchIn(viewModelScope)
    }

    suspend fun dispatchEvent(event: MovieDetailEvent) {
        eventsFlow.emit(event)
    }

}
