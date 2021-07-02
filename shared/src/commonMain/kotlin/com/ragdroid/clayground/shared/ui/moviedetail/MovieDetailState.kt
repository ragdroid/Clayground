package com.ragdroid.clayground.shared.ui.moviedetail

import com.ragdroid.clayground.shared.domain.models.MovieDetail
import com.ragdroid.clayground.shared.domain.models.MovieId
import com.ragdroid.clayground.shared.domain.repository.MovieDetailRepository
import com.ragdroid.clayground.shared.ui.base.Next
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

data class MovieDetailState(
    val loadingState: LoadingState = LoadingState.IDLE,
    val movieDetails: MovieDetail? = null
)

class MovieDetailUpdate() {
    fun update(state: MovieDetailState, event: MovieDetailEvent): Next<MovieDetailState, MovieDetailSideEffect> =
        when(event) {
            is MovieDetailEvent.Load -> {
                Next.next(
                    state.copy(loadingState = LoadingState.LOADING),
                    MovieDetailSideEffect.LoadMovieDetails(MovieId(464052))
                )
            }
            is MovieDetailEvent.Reload -> {
                Next.dispatch(MovieDetailSideEffect.LoadMovieDetails(MovieId(460465)))
//                Next.dispatch(MovieDetailSideEffect.LoadMovieDetails(MovieId(464052)))
            }
            is MovieDetailEvent.LoadSuccess -> Next.next(state.copy(
                loadingState = LoadingState.IDLE,
                movieDetails = event.movieDetails
            ))
            is MovieDetailEvent.LoadFailed -> Next.next(
                state.copy(loadingState = LoadingState.IDLE)
            )
            is MovieDetailEvent.Upvote -> Next.next(
                state.copy(movieDetails = state.movieDetails?.copy(voteCount = state.movieDetails.voteCount + 1))
            )
            is MovieDetailEvent.Downvote -> Next.next(
                state.copy(movieDetails = state.movieDetails?.copy(voteCount = state.movieDetails.voteCount - 1))
            )
            else -> Next.noChange()
        }
}

enum class LoadingState {
    LOADING, IDLE
}

sealed class MovieDetailEvent {
    object Load: MovieDetailEvent()
    object Reload: MovieDetailEvent()

    object Upvote: MovieDetailEvent()
    object Downvote: MovieDetailEvent()

    //Result events
    data class LoadSuccess(val movieDetails: MovieDetail): MovieDetailEvent()
    data class LoadFailed(val throwable: Throwable): MovieDetailEvent()
}

sealed class MovieDetailSideEffect {
    data class LoadMovieDetails(val id: MovieId): MovieDetailSideEffect()

}

@ExperimentalCoroutinesApi
class MovieDetailSideEffectHandler(
    private val movieDetailRepository: MovieDetailRepository
) {
    fun process(sideEffect: MovieDetailSideEffect, _uiEffectsFlow: ConflatedBroadcastChannel<MovieDetailViewEffect>): Flow<MovieDetailEvent> =
        when (sideEffect) {
            is MovieDetailSideEffect.LoadMovieDetails -> flow<MovieDetailEvent> {
                val movieDetail = movieDetailRepository.movieDetails(sideEffect.id.id)
                emit(MovieDetailEvent.LoadSuccess(movieDetail.copy(voteCount = movieDetail.voteCount + 1)))
            }
                .onStart { delay(3000) }
                .catch {
                _uiEffectsFlow.send(MovieDetailViewEffect.ShowError(it))
                emit(MovieDetailEvent.LoadFailed(it))
            }
        }
}

sealed class MovieDetailViewEffect {
    data class ShowError(val throwable: Throwable): MovieDetailViewEffect()
}


