package com.ragdroid.clayground.moviedetail

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ragdroid.clayground.shared.ui.moviedetail.LoadingState
import com.ragdroid.clayground.shared.ui.moviedetail.MovieDetailEvent
import com.ragdroid.clayground.shared.ui.moviedetail.MovieDetailState
import com.ragdroid.clayground.theme.MovieTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import timber.log.Timber

@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class MovieDetailActivity: AppCompatActivity() {
    private val viewModel: MovieDetailAndroidViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.dispatch(MovieDetailEvent.Load)
        setContent {
            val themeColors = if (isSystemInDarkTheme()) MovieTheme.darkColors else MovieTheme.lightColors
            MaterialTheme(themeColors) {
                val state = viewModel.stateFlow.collectAsState(MovieDetailState()).value
                Loader(loadingState = state.loadingState)
                Column {
                    MovieTitle(state.movieDetails?.title)
                }
            }
        }
    }
    @Composable
    fun Loader(loadingState: LoadingState) {
        Timber.d("recomposed loader")
        if (loadingState == LoadingState.Loading) {
            Box(Modifier.fillMaxHeight().fillMaxWidth().wrapContentSize(Alignment.Center)) {
                CircularProgressIndicator()
            }
        }
    }
    @Composable
    fun MovieTitle(title: String?) {
        Timber.d("recomposed movieTitle")
        title?.let {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center),
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}
