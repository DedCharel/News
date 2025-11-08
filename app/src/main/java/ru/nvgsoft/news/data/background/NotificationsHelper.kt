package ru.nvgsoft.news.data.background

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import ru.nvgsoft.news.R

class NotificationsHelper @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val notificationManager = context.getSystemService<NotificationManager>()

    init {
        createNotificationChanel()
    }

    private fun createNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chanel =  NotificationChannel(
                CHANEL_ID,
                context.getString(R.string.new_articles),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager?.createNotificationChannel(chanel)
        }
    }

    fun showNewArticlesNotification(topics: List<String>) {
        val notification = NotificationCompat.Builder(context, CHANEL_ID)
            .setSmallIcon(R.drawable.ic_newspaper)
            .setContentTitle(context.getString(R.string.new_articles_notification))
            .setContentText(
                context.getString(
                    R.string.update_subscriptions,
                    topics.size,
                    topics.joinToString(", ")
                )
            )
            .build()
        notificationManager?.notify(NOTIFICATION_ID, notification)
    }

    companion object{
        const val CHANEL_ID = "new_articles"
        const val NOTIFICATION_ID = 1
    }
}