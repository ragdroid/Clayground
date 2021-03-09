package com.ragdroid.clayground.moviedetail

import com.ragdroid.clayground.model.MovieId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

data class MovieDetailState(
    val loadingState: LoadingState = LoadingState.Idle
) {
    fun update(event: MovieDetailEvent,
               sideEffectsFlow: MutableSharedFlow<MovideDetailSideEffect>,
               uiEffectsFlow: MutableSharedFlow<MovieDetailState>)  =
        when(event) {
            else -> this
        }
}

sealed class LoadingState {
    object Loading: LoadingState()
    object Idle: LoadingState()
}

sealed class MovieDetailEvent {
    object Load: MovieDetailEvent()

    //Result events
    object LoadSuccess: MovieDetailEvent()
    object LoadFailed: MovieDetailEvent()
}

sealed class MovideDetailSideEffect {
    data class LoadMovieDetails(val id: MovieId): MovideDetailSideEffect()

}

fun MovideDetailSideEffect.process(_uiEffectsFlow: MutableSharedFlow<MovieDetailState>): Flow<MovieDetailEvent> =
    when(this) {
        is MovideDetailSideEffect.LoadMovieDetails -> flow<MovieDetailEvent> {
            emit(MovieDetailEvent.LoadSuccess)
        }
        else -> flowOf()
    }


