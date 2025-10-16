package ru.nvgsoft.news.domain.entity

data class Settings(
    val language: Language,
    val interval: Interval,
    val notificationEnabled: Boolean,
    val wifiOnly: Boolean
)

enum class Language{
    RUSSIAN, ENGLISH, FRENCH, GERMAN
}

enum class Interval(val minutes: Int){
    MIN_15(15),
    MIN_30(30),
    HOUR_1(60),
    HOUR_2(120),
    HOUR_4(240),
    HOUR_8(480),
    HOUR_24(1440)

}