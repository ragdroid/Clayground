package com.ragdroid.clayground.shared.ui.moviedetail

import co.touchlab.kermit.Kermit
import com.ragdroid.clayground.shared.domain.repository.MovieDetailRepository
import com.ragdroid.clayground.shared.ui.base.GenericViewModel

class MovieDetailViewModel(
    val movieDetailRepository: MovieDetailRepository,
    kermit: Kermit
): GenericViewModel<MovieDetailState, MovieDetailEvent, MovieDetailSideEffect, MovieDetailViewEffect>(
    kermit,
    MovieDetailInit(),
    MovieDetailUpdate(),
    MovieDetailSideEffectHandler(movieDetailRepository))