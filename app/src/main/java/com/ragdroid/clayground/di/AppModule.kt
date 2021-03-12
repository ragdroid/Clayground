package com.ragdroid.clayground.di

import com.ragdroid.clayground.shared.api.MoviesService
import com.ragdroid.clayground.shared.di.SharedModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun MoviesService(): MoviesService = SharedModule.moviesService
}
