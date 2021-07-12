package com.ragdroid.clayground.moviedetail

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ragdroid.clayground.shared.ui.base.GenericViewModel
import com.ragdroid.clayground.shared.ui.base.MviViewModel
import com.ragdroid.clayground.shared.ui.moviedetail.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@HiltViewModel
class MovieDetailAndroidViewModel @Inject constructor(
    private val movieDetailViewModel: MovieDetailViewModel
):ViewModel(), MviViewModel<MovieDetailState, MovieDetailEvent, MovieDetailViewEffect> by movieDetailViewModel {

    init {
        movieDetailViewModel.initializeIn(viewModelScope)
    }

    fun dispatch(event: MovieDetailEvent) {
        viewModelScope.launch {
            movieDetailViewModel.dispatchEvent(event)
        }
    }

}
