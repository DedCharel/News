package ru.nvgsoft.news.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsResponseDto(
    @SerialName("articles") var articles: List<ArticleDto> = listOf()
)
