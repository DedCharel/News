package ru.nvgsoft.news.di

import android.content.Context
import androidx.work.WorkManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import ru.nvgsoft.news.data.local.NewsDao
import ru.nvgsoft.news.data.local.NewsDatabase
import ru.nvgsoft.news.data.remote.NewsApiService
import ru.nvgsoft.news.data.repository.NewsRepositoryImpl
import ru.nvgsoft.news.domain.repository.NewsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindNewsRepository(
        impl: NewsRepositoryImpl
    ): NewsRepository

    companion object {

        @Singleton
        @Provides
        fun provideWorkManager(
            @ApplicationContext conext: Context
        ): WorkManager = WorkManager.getInstance(conext)


        @Singleton
        @Provides
        fun provideJson(): Json {
            return Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }
        }

        @Singleton
        @Provides
        fun provideConverterFactory(
            json: Json
        ): Converter.Factory{
           return json.asConverterFactory("application/json".toMediaType())
        }

        @Singleton
        @Provides
        fun provideRetrofit(
            converterFactory: Converter.Factory
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(converterFactory)
                .build()
        }

        @Singleton
        @Provides
        fun provideApiService(
            retrofit: Retrofit
        ): NewsApiService {
            return retrofit.create()
        }

        @Singleton
        @Provides
        fun provideNewsDatabase(
            @ApplicationContext context: Context
        ): NewsDatabase {
            return NewsDatabase.getInstance(context)
        }

        @Singleton
        @Provides
        fun provideNewsDao(
            database: NewsDatabase
        ): NewsDao = database.newsDao()

    }
}