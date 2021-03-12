package com.ragdroid.clayground.shared.domain.models


data class MovieDetail(
    val id: Int,
    val backdropPath: String?,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val releaseDate: String?,
    val runtime: Int,
    val voteAverage: Double,
    val voteCount: Int
)
