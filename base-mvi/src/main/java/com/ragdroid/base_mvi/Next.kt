package com.ragdroid.base_mvi

data class Next<S,F>(val next: S? = null, val effects: List<F> = emptyList()) {
    fun hasModel() = next != null
    fun safeModel() = next!!
    fun hasEffects() = effects.isNotEmpty()
    fun effects() = effects
    companion object {
        fun <S,F> noChange() = Next<S,F>()
        fun <S,F> dispatch(vararg effects: F) = Next<S,F>(effects = effects.toList())
        fun <S,F> next(model: S, vararg effects: F) = Next<S,F>(model, effects.toList())
    }
}
