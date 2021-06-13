package com.ragdroid.clayground.moviedetail

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.ragdroid.clayground.shared.domain.models.MovieDetail
import com.ragdroid.clayground.shared.ui.moviedetail.LoadingState

data class MovieUIState(
    val loadingState: LoadingState = LoadingState.Idle,
    val movieDetails: MovieDetail? = null
)