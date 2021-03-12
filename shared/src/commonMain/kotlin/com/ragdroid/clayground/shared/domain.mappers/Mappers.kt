package com.ragdroid.clayground.shared.domain.mappers

import com.ragdroid.clayground.shared.api.models.MovieDetailResponse
import com.ragdroid.clayground.shared.domain.models.MovieDetail

fun MovieDetailResponse.toMovieDetail(): MovieDetail {
    return MovieDetail(
        id = id,
        backdropPath = backdropPath,
        title = title,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        runtime = runtime,
        voteAverage = voteAverage,
        voteCount = voteCount
    )
}
