package com.ragdroid.clayground.shared.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailResponse(
    val id: Int,
    @SerialName("backdrop_path") val backdropPath: String?,
    val title: String,
    val overview: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("release_date") val releaseDate: String?,
    val genres: List<MovieMetaInfo>,
    val runtime: Int,
    @SerialName("production_companies") val productionCompanies: List<MovieMetaInfo>,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Int
)

@Serializable
data class MovieMetaInfo (
    val id: Int,
    val name: String)
