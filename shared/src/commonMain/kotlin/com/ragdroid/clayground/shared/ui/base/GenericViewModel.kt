package com.ragdroid.clayground.shared.ui.base

import co.touchlab.kermit.Kermit
import co.touchlab.stately.ensureNeverFrozen
import com.ragdroid.clayground.shared.domain.repository.MovieDetailRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlin.contracts.Effect

@FlowPreview
@ExperimentalCoroutinesApi
open class GenericViewModel<State, Event, SideEffect, ViewEffect>(
    private val kermit: Kermit,
    private val init: Init<State>,
    private val update: Update<State, Event, SideEffect>,
    private val effectHandler: SideEffectHandler<Event, SideEffect, ViewEffect>
): GenericNativeViewModel<State, Event, ViewEffect>() {

    init {
        ensureNeverFrozen()
    }
    private val eventsFlow = Channel<Event>(Channel.UNLIMITED)
    private val sideEffectsFlow = Channel<SideEffect>(Channel.UNLIMITED)

    //TODO single live event, should we use channel with buffer 0? we shouldn't conflate in this case
    //we need all nav events
    private val _uiEffectsFlow = Channel<ViewEffect>()
    override val uiEffectsFlow: Flow<ViewEffect>
        get() = _uiEffectsFlow.receiveAsFlow()

    private val _stateFlow = MutableStateFlow(init.init())
    override val stateFlow: Flow<State>
        get() = _stateFlow

    override fun initializeIn(viewModelScope: CoroutineScope) {
        val resultsFlow = sideEffectsFlow
            .consumeAsFlow()
            .flatMapMerge {
                kermit.d { "Side Effect: $it" }
                effectHandler.process(it, _uiEffectsFlow)
            }
        merge(eventsFlow.consumeAsFlow(), resultsFlow)
            .onEach {
                kermit.d { "Event: $it" }
            }
            //TODO single init state?
            .scan(init.init()) { state, event ->
                val next = update.update(state, event ?: return@scan state)
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

    override suspend fun dispatchEvent(event: Event) {
        kermit.d {
            "inside GenericViewModel: dispatch Event with $event"
        }

        eventsFlow.send(event)
    }

}
