package com.ragdroid.clayground.di

import com.ragdroid.clayground.shared.di.SharedModule
import com.ragdroid.clayground.shared.ui.moviedetail.MovieDetailViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideSharedModule() = SharedModule()

    @Provides
    fun provideMovieDetailRepository(): MovieDetailViewModel = SharedModule.movieDetailViewModel
}
