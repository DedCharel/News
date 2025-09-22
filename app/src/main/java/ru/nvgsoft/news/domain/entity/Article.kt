package ru.nvgsoft.news.domain.entity

data class Article(
    val title: String,
    val descriptions: String,
    val imageUrl: String?,
    val sourceName: String,
    val publishedAt: Long,
    val url: String
)
