package ru.nvgsoft.news.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleDto(
    @SerialName("source") var source: SourceDto = SourceDto(),
    @SerialName("title") var title: String = "",
    @SerialName("description") var description: String = "",
    @SerialName("url") var url: String = "",
    @SerialName("urlToImage") var urlToImage: String? = "",
    @SerialName("publishedAt") var publishedAt: String = ""
)
