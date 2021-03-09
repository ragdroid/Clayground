package com.ragdroid.clayground

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MoviesApplication: Application() {

    override fun onCreate() {
        plantTrees()
        super.onCreate()
    }

    private fun plantTrees() {
        Timber.plant(Timber.DebugTree())
    }
}
