package ru.nvgsoft.news.data.maper


import ru.nvgsoft.news.data.local.ArticleDbModel
import ru.nvgsoft.news.data.remote.NewsResponseDto
import ru.nvgsoft.news.domain.entity.Article
import ru.nvgsoft.news.domain.entity.Interval
import java.text.SimpleDateFormat
import java.util.Locale

fun NewsResponseDto.toDBModels(topic: String): List<ArticleDbModel> {
    return articles.map {
        ArticleDbModel(
            title = it.title,
            descriptions = it.description,
            imageUrl = it.urlToImage,
            sourceName = it.source.name,
            publishedAt = it.publishedAt.toTimestamp(),
            url = it.url,
            topic = topic
        )
    }

}

fun Int.toInterval(): Interval {
   return Interval.entries.first(){it.minutes == this}
}

fun List<ArticleDbModel>.toEntities(): List<Article> {
    return map {
        Article(
            title = it.title,
            descriptions = it.descriptions,
            imageUrl = it.imageUrl,
            sourceName = it.sourceName,
            publishedAt = it.publishedAt,
            url = it.url
        )
    }.distinct() // чтобы не было повторов статей
}

private fun String.toTimestamp(): Long {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    return dateFormatter.parse(this)?.time ?: System.currentTimeMillis()
}