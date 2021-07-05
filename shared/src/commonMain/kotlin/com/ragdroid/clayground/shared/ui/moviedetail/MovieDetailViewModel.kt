package com.ragdroid.clayground.shared.ui.moviedetail

import co.touchlab.kermit.Kermit
import co.touchlab.stately.ensureNeverFrozen
import com.ragdroid.clayground.shared.domain.repository.MovieDetailRepository
import com.ragdroid.clayground.shared.ui.base.GenericNativeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.scan

@FlowPreview
@ExperimentalCoroutinesApi
class MovieDetailViewModel(
    private val movieDetailRepository: MovieDetailRepository,
    private val kermit: Kermit
): GenericNativeViewModel<MovieDetailState, MovieDetailEvent, MovieDetailViewEffect>() {

    init {
        ensureNeverFrozen()
    }
    //TODO should this be a normal channel instead?
    private val eventsFlow = Channel<MovieDetailEvent>(Channel.UNLIMITED)
    private val sideEffectsFlow = Channel<MovieDetailSideEffect>(Channel.UNLIMITED)

    //TODO single live event, should we use channel with buffer 0? we shouldn't conflate in this case
    //we need all nav events
    private val _uiEffectsFlow = ConflatedBroadcastChannel<MovieDetailViewEffect>()
    override val uiEffectsFlow: Flow<MovieDetailViewEffect>
        get() = _uiEffectsFlow.asFlow()

    private val _stateFlow = MutableStateFlow(MovieDetailState())
    override val stateFlow: Flow<MovieDetailState>
        get() = _stateFlow

    override fun initializeIn(viewModelScope: CoroutineScope) {
        val resultsFlow = sideEffectsFlow
            .consumeAsFlow()
            .flatMapMerge {
                kermit.d { "Side Effect: $it" }
                val movieDetailSideEffectHandler = MovieDetailSideEffectHandler(movieDetailRepository)
                movieDetailSideEffectHandler.process(it, _uiEffectsFlow)
            }
        merge(eventsFlow.consumeAsFlow(), resultsFlow)
            .onEach {
                kermit.d { "Event: $it" }
            }
            .scan(MovieDetailState()) { state, event ->
                val next = MovieDetailUpdate().update(state, event ?: return@scan state)
                next.effects.forEach {
                    sideEffectsFlow.send(it)
                }
                if (next.hasModel()) next.safeModel() else state
            }
            .distinctUntilChanged { old, new -> old == new }
            .onEach {
                _stateFlow.value = it
                kermit.d { "State $it" }
            }
            .catch {
                kermit.d {
                    it.printStackTrace()
                    "Error $it"
                }
            }
            .launchIn(viewModelScope)
    }

    override suspend fun dispatchEvent(event: MovieDetailEvent) {
        kermit.d {
            "inside MovieDetailViewModel: dispatch Event with $event"
        }

        eventsFlow.send(event)
    }

}
