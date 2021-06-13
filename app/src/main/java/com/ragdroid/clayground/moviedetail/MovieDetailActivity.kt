package com.ragdroid.clayground.moviedetail

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ragdroid.clayground.shared.ui.moviedetail.LoadingState
import com.ragdroid.clayground.shared.ui.moviedetail.MovieDetailEvent
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
                Column {
                    MovieDetail()
                    RefreshButton()
//                    ShowLoaderButton()
//                    HideLoaderButton()
                }
            }
        }
    }
    @Composable
    fun RefreshButton() {
        Timber.d("recomposed RefreshButton")
        Button(onClick = {
            viewModel.dispatch(MovieDetailEvent.Reload)
        }) {
            Text(text = "Reload")
        }
    }
//    @Composable
//    fun ShowLoaderButton() {
//        Timber.d("recomposed ShowLoaderButton")
//        Button(onClick = {
//            viewModel.dispatch(MovieDetailEvent.Reload)
//        }) {
//            Text(text = "Show Loader")
//        }
//    }

//    @Composable
//    fun HideLoaderButton() {
//        Timber.d("recomposed HideLoaderButton")
//        Button(onClick = {
//            viewModel.dispatch(MovieDetailEvent.Reload)
//        }) {
//            Text(text = "Hide Loader")
//        }
//    }


    @Composable
    fun MovieDetail() {
        val mapState = viewModel.mapState()
        val state: MovieUIState by mapState.collectAsState(initial = MovieUIState())

        Timber.d("recomposed MovieDetail state: %s", state)
        Column {
            Loader(loadingState = state.loadingState)
            MovieTitle(state.movieDetails?.title)
        }
    }

    @Composable
    fun Loader(loadingState: LoadingState) {
        Timber.d("recomposed Loader loadingState: %s", loadingState)
        if (loadingState == LoadingState.Loading) {
            Box(
                Modifier
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator()
            }
        }
    }

    @Composable
    fun MovieTitle(title: String?) {
        Timber.d("recomposed MovieTitle title: %s", title)
        Column {
            title?.let {
                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center),
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
    }
}
