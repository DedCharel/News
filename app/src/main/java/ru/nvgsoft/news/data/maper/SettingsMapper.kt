package ru.nvgsoft.news.data.maper

import ru.nvgsoft.news.domain.entity.RefreshConfig
import ru.nvgsoft.news.domain.entity.Settings

fun Settings.toRefreshConfig(): RefreshConfig = RefreshConfig(language, interval, wifiOnly)