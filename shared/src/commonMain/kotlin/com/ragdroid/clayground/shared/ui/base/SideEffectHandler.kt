package com.ragdroid.clayground.shared.ui.base

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

interface SideEffectHandler<Event, SideEffect, ViewEffect> {
    fun process(sideEffect: SideEffect, viewEffectConsumer: Channel<ViewEffect>): Flow<Event>
}