package com.ragdroid.clayground.moviedetail

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.glide.rememberGlidePainter
import com.ragdroid.clayground.shared.ui.moviedetail.LoadingState
import com.ragdroid.clayground.shared.ui.moviedetail.MovieDetailEvent
import com.ragdroid.clayground.shared.ui.moviedetail.MovieDetailState
import com.ragdroid.clayground.theme.MovieTheme
import com.ragdroid.clayground.util.LogCompositions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import com.ragdroid.clayground.R
import com.ragdroid.clayground.shared.ui.moviedetail.MovieDetailViewEffect
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class MovieDetailActivity: AppCompatActivity() {

    private val viewModel: MovieDetailAndroidViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.dispatch(MovieDetailEvent.Load)
        setContent {
            val themeColors = MovieTheme.darkColors
//            val themeColors = if (isSystemInDarkTheme()) MovieTheme.darkColors else MovieTheme.lightColors
            MaterialTheme(themeColors) {
                MovieDetailScreen()
            }
        }
    }

    @Composable
    fun MovieDetailScreen() {
        val scrollState = rememberScrollState()
        val scaffoldState = rememberScaffoldState()
        Scaffold(
            scaffoldState = scaffoldState,
            content = {
                Column(Modifier.verticalScroll(scrollState)) {
                    MovieDetail()
                    HandleViewEffects(scaffoldState)
                    RefreshButton()
                    UpvoteButton()
                    DownvoteButton()
                }
            }
        )
    }

    @Composable
    fun HandleViewEffects(scaffoldState: ScaffoldState) {
        val defaultErrorMessage = stringResource(id = R.string.error_movie_details)
        LaunchedEffect("launch error message") {
            viewModel.uiEffectsFlow.onEach {
                when (it) {
                    is MovieDetailViewEffect.ShowError -> scaffoldState.snackbarHostState.showSnackbar(it.throwable.message ?: defaultErrorMessage)
                }
            }.collect()
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
        Box(contentAlignment = Alignment.BottomStart) {
            Loader(loadingState = state.loadingState)
            val movieDetails = state.movieDetails
            movieDetails?.let { movieDetail ->
                //TODO?
                MovieBackdrop(movieDetail.backdropPath)
                Column(
                    Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    MovieTitle(title = movieDetail.title)
                    MovieOverview(overview = movieDetail.overview)
                    MovieVotes(voteCount = movieDetail.voteCount)
                    MovieLength(length = movieDetail.runtime)
                    MovieReleaseDate(releaseDate = movieDetail.releaseDate)
                }
            }
        }
    }

    @Composable
    private fun MovieBackdrop(backdropPath: String?) {
        LogCompositions("MovieDetailActivity","recomposed MovieBackdrop: $backdropPath")
        Image(
            painter = rememberGlidePainter(
                request = backdropPath
            ),
            contentDescription = stringResource(id = R.string.movie_backdrop_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(500.dp)
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopCenter),
        )
    }

    @Composable
    fun Loader(loadingState: LoadingState) {
        LogCompositions("MovieDetailActivity","recomposed Loader loadingState: $loadingState")
        if (loadingState == LoadingState.LOADING) {
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
        LogCompositions("MovieDetailActivity","recomposed MovieTitle title: $title")
        title?.let {
            Text(
                text = title,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.CenterStart),
                color = MaterialTheme.colors.onBackground
            )
        }
    }

    @Composable
    fun MovieOverview(overview: String) {
        LogCompositions("MovieDetailActivity","recomposed MovieOverview overview: $overview")
        Text(
            text = overview,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.CenterStart),
            color = MaterialTheme.colors.onBackground
        )
    }

    @Composable
    fun MovieVotes(voteCount: Int) {
        LogCompositions("MovieDetailActivity","recomposed MovieVotes voteCount: $voteCount")
        Text(
            text = voteCount.toString(),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.CenterStart),
            color = MaterialTheme.colors.onBackground
        )
    }

    @Composable
    fun MovieLength(length: Int) {
        LogCompositions("MovieDetailActivity","recomposed MovieLenght length: $length")
        Text(
            text = length.toString(),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.CenterStart),
            color = MaterialTheme.colors.onBackground
        )
    }

    @Composable
    fun MovieReleaseDate(releaseDate: String?) {
        LogCompositions("MovieDetailActivity","recomposed MovieReleaseDate releaseDate: $releaseDate")
        releaseDate?.let {
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
