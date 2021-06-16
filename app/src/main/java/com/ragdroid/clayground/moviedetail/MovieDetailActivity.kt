package com.ragdroid.clayground.moviedetail

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ragdroid.clayground.shared.ui.moviedetail.LoadingState
import com.ragdroid.clayground.shared.ui.moviedetail.MovieDetailEvent
import com.ragdroid.clayground.shared.ui.moviedetail.MovieDetailState
import com.ragdroid.clayground.theme.MovieTheme
import com.ragdroid.clayground.util.LogCompositions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

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
                Column(Modifier.padding(16.dp)) {
                    MovieDetail()
                    RefreshButton()
                    UpvoteButton()
                    DownvoteButton()
                }
            }
        }
    }
    @Composable
    fun RefreshButton() {
        LogCompositions("MovieDetailActivity","recomposed RefreshButton")
        Button(onClick = {
            viewModel.dispatch(MovieDetailEvent.Reload)
        }) {
            Text(text = "Reload")
        }
    }

    @Composable
    fun UpvoteButton() {
        LogCompositions("MovieDetailActivity","recomposed UpvoteButton")
        Button(onClick = {
            viewModel.dispatch(MovieDetailEvent.Upvote)
        }) {
            Text(text = "Upvote")
        }
    }

    @Composable
    fun DownvoteButton() {
        LogCompositions("MovieDetailActivity","recomposed DownvoteButton")
        Button(onClick = {
            viewModel.dispatch(MovieDetailEvent.Downvote)
        }) {
            Text(text = "Downvote")
        }
    }


    @Composable
    fun MovieDetail() {
        val state: MovieDetailState by viewModel.stateFlow.collectAsState(initial = MovieDetailState())

        LogCompositions("MovieDetailActivity","recomposed MovieDetail state: $state")
        Column {
            Loader(loadingState = state.loadingState)
            val movieDetails = state.movieDetails
            movieDetails?.let { movieDetail ->
                MovieTitle(title = movieDetail.title)
                MovieOverview(overview = movieDetail.overview)
                MovieVotes(voteCount = movieDetail.voteCount)
                MovieLength(length = movieDetail.runtime)
                MovieReleaseDate(releaseDate = movieDetail.releaseDate)
            }

        }
    }

    @Composable
    fun Loader(loadingState: LoadingState) {
        LogCompositions("MovieDetailActivity","recomposed Loader loadingState: $loadingState")
        if (loadingState == LoadingState.LOADING) {
            Box(
                Modifier
                    .wrapContentSize(Alignment.CenterStart)
            ) {
                CircularProgressIndicator()
            }
        }
    }

    @Composable
    fun MovieTitle(title: String?) {
        LogCompositions("MovieDetailActivity","recomposed MovieTitle title: $title")
        Column {
            title?.let {
                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.CenterStart),
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
    }

    @Composable
    fun MovieOverview(overview: String) {
        LogCompositions("MovieDetailActivity","recomposed MovieOverview overview: $overview")
        Column {
            Text(
                text = overview,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.CenterStart),
                color = MaterialTheme.colors.onBackground
            )
        }
    }

    @Composable
    fun MovieVotes(voteCount: Int) {
        LogCompositions("MovieDetailActivity","recomposed MovieVotes voteCount: $voteCount")
        Column {
            Text(
                text = voteCount.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.CenterStart),
                color = MaterialTheme.colors.onBackground
            )
        }
    }

    @Composable
    fun MovieLength(length: Int) {
        LogCompositions("MovieDetailActivity","recomposed MovieLenght length: $length")
        Column {
            Text(
                text = length.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.CenterStart),
                color = MaterialTheme.colors.onBackground
            )
        }
    }

    @Composable
    fun MovieReleaseDate(releaseDate: String?) {
        LogCompositions("MovieDetailActivity","recomposed MovieReleaseDate releaseDate: $releaseDate")
        releaseDate?.let {
            Column {
                Text(
                    text = releaseDate,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.CenterStart),
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
    }
}
