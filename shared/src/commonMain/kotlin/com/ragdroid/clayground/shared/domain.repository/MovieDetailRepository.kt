package com.ragdroid.clayground.shared.domain.repository

import com.ragdroid.clayground.shared.api.MoviesService
import com.ragdroid.clayground.shared.domain.mappers.toMovieDetail
import com.ragdroid.clayground.shared.domain.models.MovieDetail

class MovieDetailRepository(
    private val moviesService: MoviesService
) {
    suspend fun movieDetails(id: Int): MovieDetail {
        return moviesService.movieDetail(id).toMovieDetail()
    }
}
