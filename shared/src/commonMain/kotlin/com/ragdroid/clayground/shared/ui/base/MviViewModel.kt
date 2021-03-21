package com.ragdroid.clayground.shared.ui.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface MviViewModel<State, Event, ViewEffect> {
    val uiEffectsFlow: Flow<ViewEffect>
    val stateFlow: Flow<State>
    suspend fun dispatchEvent(event: Event)
    fun initializeIn(viewModelScope: CoroutineScope)
}
