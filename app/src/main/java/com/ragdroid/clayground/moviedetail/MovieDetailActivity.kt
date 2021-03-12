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
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ragdroid.clayground.theme.MovieTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class MovieDetailActivity: AppCompatActivity() {
    private val viewModel: MovieDetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.dispatchEvent(MovieDetailEvent.Load)
        setContent {
            val themeColors = if (isSystemInDarkTheme()) MovieTheme.darkColors else MovieTheme.lightColors
            MaterialTheme(themeColors) {
                val state = viewModel.stateFlow.collectAsState().value
                Loader(loadingState = state.loadingState)
                Column {
                    MovieTitle(state.movieDetails?.title)
                }
            }
        }
    }
    @Composable
    fun Loader(loadingState: LoadingState) {
        if (loadingState == LoadingState.Loading) {
            Box(Modifier.fillMaxHeight().fillMaxWidth().wrapContentSize(Alignment.Center)) {
                CircularProgressIndicator()
            }
        }
    }
    @Composable
    fun MovieTitle(title: String?) {
        title?.let {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center),
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}
