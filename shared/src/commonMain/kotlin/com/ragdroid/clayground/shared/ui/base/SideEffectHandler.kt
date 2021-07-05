package com.ragdroid.clayground.shared.ui.base

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow

interface SideEffectHandler<Event, SideEffect, ViewEffect> {
    fun process(sideEffect: SideEffect, viewEffectConsumer: ConflatedBroadcastChannel<ViewEffect>): Flow<Event>
}