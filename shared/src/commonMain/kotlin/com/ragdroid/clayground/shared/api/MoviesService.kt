package com.ragdroid.clayground.shared.api

import com.ragdroid.clayground.shared.api.models.MovieDetailResponse

interface MoviesService {
    suspend fun movieDetail(id: Int): MovieDetailResponse
}
