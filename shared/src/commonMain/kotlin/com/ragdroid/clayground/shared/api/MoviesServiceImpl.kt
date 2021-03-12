package com.ragdroid.clayground.shared.api

import com.ragdroid.clayground.shared.api.models.MovieDetailResponse
import io.ktor.client.*
import io.ktor.client.request.*

class MoviesServiceImpl(
    private val baseUrl: BaseUrl,
    private val apiToken: ApiToken,
    private val httpClient: HttpClient
    ): MoviesService {
    override suspend fun movieDetail(id: Int): MovieDetailResponse {
        return httpClient.get("${baseUrl.urlString}/movie/$id") {
         url {
             header("Authorization", "Bearer ${apiToken.token}")
         }
        }
    }
}
