package com.ragdroid.clayground.shared.di

import co.touchlab.kermit.Kermit
import com.ragdroid.clayground.shared.api.ApiToken
import com.ragdroid.clayground.shared.api.BaseUrl
import com.ragdroid.clayground.shared.api.MoviesService
import com.ragdroid.clayground.shared.api.MoviesServiceImpl
import com.ragdroid.clayground.shared.ui.moviedetail.MovieDetailViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import com.ragdroid.clayground.shared.BuildKonfig
import com.ragdroid.clayground.shared.domain.repository.MovieDetailRepository
import com.ragdroid.clayground.shared.kermitLogger
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import kotlin.native.concurrent.ThreadLocal

class SharedModule {
    val apiModule = module {
        single {
            val apiToken: ApiToken = get()
            //providing json as a dependency separately causes kotlin native to freeze, not sure why yet
            val json = Json {
                ignoreUnknownKeys = true
            }
            HttpClient {
                install(JsonFeature) {
                    serializer = KotlinxSerializer(json)
                }
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.INFO
                }
            }
        }
        single { BaseUrl("https://api.themoviedb.org/3") }
        single { ApiToken(BuildKonfig.TMDB_API_TOKEN) }
        single { MovieDetailRepository(get()) }
        single { MoviesServiceImpl(get(), get(), get()) as MoviesService }
        single { Kermit(kermitLogger()) }
        single { MovieDetailViewModel(get(), get()) }
    }
    fun configure() {
        startKoin {
            modules(apiModule)
        }
    }

    companion object {
        val movieDetailViewModel: MovieDetailViewModel
            get() = CommonModule.get().get<MovieDetailViewModel>()
        val kermitLogger: Kermit
            get() = CommonModule.get().get<Kermit>()
    }
}


