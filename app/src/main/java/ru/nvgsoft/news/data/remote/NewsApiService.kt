package ru.nvgsoft.news.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ru.nvgsoft.news.BuildConfig

interface NewsApiService {

    @GET("v2/everything?apiKey=${BuildConfig.NEWS_API_KEY}")
    suspend fun loadArticles(
        @Query("q") topic:String,
        @Query("language") language: String
    ): NewsResponseDto
}