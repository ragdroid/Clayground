package com.ragdroid.clayground.moviedetail

import com.ragdroid.base_mvi.Next
import com.ragdroid.clayground.model.MovieId
import com.ragdroid.clayground.shared.api.MoviesService
import com.ragdroid.clayground.shared.api.models.MovieDetailResponse
import com.ragdroid.clayground.shared.domain.mappers.toMovieDetail
import com.ragdroid.clayground.shared.domain.models.MovieDetail
import com.ragdroid.clayground.shared.domain.repository.MovieDetailRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import java.util.concurrent.TimeUnit

data class MovieDetailState(
    val loadingState: LoadingState = LoadingState.Idle,
    val movieDetails: MovieDetail? = null
)

class MovieDetailUpdate() {
    fun update(state: MovieDetailState, event: MovieDetailEvent):Next<MovieDetailState, MovieDetailSideEffect>  =
        when(event) {
            is MovieDetailEvent.Load -> {
                Next.next(
                    state.copy(loadingState = LoadingState.Loading),
                    MovieDetailSideEffect.LoadMovieDetails(MovieId(464052))
                )
            }
            is MovieDetailEvent.LoadSuccess -> Next.next(state.copy(
                loadingState = LoadingState.Idle,
                movieDetails = event.movieDetails
            ))
            is MovieDetailEvent.LoadFailed -> Next.next(
                state.copy(loadingState = LoadingState.Idle)
            )
            else -> Next.noChange()
        }
}

sealed class LoadingState {
    object Loading: LoadingState()
    object Idle: LoadingState()
}

sealed class MovieDetailEvent {
    object Load: MovieDetailEvent()

    //Result events
    data class LoadSuccess(val movieDetails: MovieDetail): MovieDetailEvent()
    data class LoadFailed(val throwable: Throwable): MovieDetailEvent()
}

sealed class MovieDetailSideEffect {
    data class LoadMovieDetails(val id: MovieId): MovieDetailSideEffect()

}

class MovieDetailSideEffectHandler(
    private val movieDetailRepository: MovieDetailRepository
) {
    fun process(sideEffect: MovieDetailSideEffect, _uiEffectsFlow: MutableSharedFlow<MovieDetailViewEffect>): Flow<MovieDetailEvent> =
        when (sideEffect) {
            is MovieDetailSideEffect.LoadMovieDetails -> flow<MovieDetailEvent> {
                val movieDetail = movieDetailRepository.movieDetails(sideEffect.id.id)
                emit(MovieDetailEvent.LoadSuccess(movieDetail))
            }
                .onStart { delay(3000) }
                .catch {
                _uiEffectsFlow.emit(MovieDetailViewEffect.ShowError(it))
                emit(MovieDetailEvent.LoadFailed(it))
            }
        }
}

sealed class MovieDetailViewEffect {
    data class ShowError(val throwable: Throwable): MovieDetailViewEffect()
}


