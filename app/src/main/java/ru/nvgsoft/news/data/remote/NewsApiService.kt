package ru.nvgsoft.news.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("v2/everything?apiKey=") // don't forget to insert the apiKey
    suspend fun loadArticles(
        @Query("q") topic:String
    ): NewsResponseDto
}