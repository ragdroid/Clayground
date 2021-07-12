package com.ragdroid.clayground.shared.ui.base

interface Update<State, Event, SideEffect> {
    fun update(state: State, event: Event): Next<State, SideEffect>
}