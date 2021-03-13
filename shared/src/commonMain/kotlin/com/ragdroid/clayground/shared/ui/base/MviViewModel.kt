package com.ragdroid.clayground.shared.ui.base

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MviViewModel<S, VF> {
    val uiEffectsFlow: SharedFlow<VF>
    val stateFlow: StateFlow<S>
}
