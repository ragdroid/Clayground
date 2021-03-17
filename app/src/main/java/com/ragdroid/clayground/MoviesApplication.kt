package com.ragdroid.clayground

import android.app.Application
import com.ragdroid.clayground.shared.di.SharedModule
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MoviesApplication: Application() {

    @Inject
    lateinit var sharedModule: SharedModule

    override fun onCreate() {
        super.onCreate()
        plantTrees()
        sharedModule.configure()
    }

    private fun plantTrees() {
        Timber.plant(Timber.DebugTree())
    }
}
