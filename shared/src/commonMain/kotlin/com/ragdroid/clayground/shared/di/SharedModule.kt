package com.ragdroid.clayground.shared.di

import com.ragdroid.clayground.shared.api.ApiToken
import com.ragdroid.clayground.shared.api.BaseUrl
import com.ragdroid.clayground.shared.api.MoviesService
import com.ragdroid.clayground.shared.api.MoviesServiceImpl
import org.koin.core.context.startKoin
import org.koin.dsl.module
import com.ragdroid.clayground.shared.BuildKonfig
import com.ragdroid.clayground.shared.domain.repository.MovieDetailRepository
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import kotlin.native.concurrent.ThreadLocal

object SharedModule {
    val apiModule = module {
        single {
            val apiToken: ApiToken = get()
            HttpClient {
                install(JsonFeature) {
                    serializer = KotlinxSerializer(get())
                }
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.INFO
                }
            }
        }
        single {
            Json {
                ignoreUnknownKeys = true
            }
        }
        single { MoviesServiceImpl(get(), get(), get()) }
        factory { BaseUrl("https://api.themoviedb.org/3") }
        factory { ApiToken(BuildKonfig.TMDB_API_TOKEN) }
        factory { MovieDetailRepository(get()) }
        factory { MoviesServiceImpl(get(), get(), get()) as MoviesService }
    }
    fun configure() {
        startKoin {
            modules(apiModule)
        }
    }

    val moviesService: MovieDetailRepository by lazy(LazyThreadSafetyMode.NONE) {
        CommonModule.get().get<MovieDetailRepository>()
    }
}


