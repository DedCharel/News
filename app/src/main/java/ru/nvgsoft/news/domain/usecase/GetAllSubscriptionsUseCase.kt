package ru.nvgsoft.news.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.nvgsoft.news.domain.repository.NewsRepository
import javax.inject.Inject

class GetAllSubscriptionsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
     operator fun invoke(): Flow<List<String>> {
        return repository.getAllSubscriptions()
     }
}