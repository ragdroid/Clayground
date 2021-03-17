package com.ragdroid.clayground.shared.domain.repository

import co.touchlab.stately.ensureNeverFrozen
import com.ragdroid.clayground.shared.api.MoviesService
import com.ragdroid.clayground.shared.domain.mappers.toMovieDetail
import com.ragdroid.clayground.shared.domain.models.MovieDetail

class MovieDetailRepository(
    private val moviesService: MoviesService
) {
    init {
        ensureNeverFrozen()
    }

    suspend fun movieDetails(id: Int): MovieDetail {
        return moviesService.movieDetail(id).toMovieDetail()
    }
}
