package com.ragdroid.clayground.shared.ui.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MviViewModel<State, Event, ViewEffect> {
    val uiEffectsFlow: Flow<ViewEffect>
    val stateFlow: Flow<State>
    suspend fun dispatchEvent(event: Event)
    fun initializeIn(viewModelScope: CoroutineScope)
}
