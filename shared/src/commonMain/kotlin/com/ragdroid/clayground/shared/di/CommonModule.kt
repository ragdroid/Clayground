package com.ragdroid.clayground.shared.di

import org.koin.core.component.KoinComponent

object CommonModule: KoinComponent {
    fun get() = getKoin()
}
