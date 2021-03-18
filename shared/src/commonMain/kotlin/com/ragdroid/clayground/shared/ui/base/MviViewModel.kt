package com.ragdroid.clayground.shared.ui.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MviViewModel<State, Event, ViewEffect> {
    val uiEffectsFlow: SharedFlow<ViewEffect>
    val stateFlow: StateFlow<State>
    suspend fun dispatchEvent(event: Event)
    fun initializeIn(viewModelScope: CoroutineScope)
}
