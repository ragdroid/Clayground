package com.ragdroid.clayground

import android.app.Application
import com.ragdroid.clayground.shared.di.SharedModule
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MoviesApplication: Application() {

    override fun onCreate() {
        plantTrees()
        SharedModule.configure()
        super.onCreate()
    }

    private fun plantTrees() {
        Timber.plant(Timber.DebugTree())
    }
}
