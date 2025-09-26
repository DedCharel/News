package ru.nvgsoft.news.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ArticleDbModel::class, SubscriptionDbModel::class],
    version = 2,
    exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    companion object {
        private var instance: NewsDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context): NewsDatabase {
            instance?.let { return it }

            synchronized(LOCK) {
                instance?.let { return it }

                return Room.databaseBuilder(
                    context = context,
                    name = "news.db",
                    klass = NewsDatabase::class.java
                ).fallbackToDestructiveMigration(true)
                    .build().also {
                        instance = it
                    }
            }
        }
    }
}