package ru.nvgsoft.news.presentation.startup

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nvgsoft.news.domain.usecase.StartRefreshDataUseCase
import javax.inject.Inject

class AppStartupManager @Inject constructor(
    private val startRefreshDataUseCase: StartRefreshDataUseCase
) {

    private val scope = CoroutineScope(Dispatchers.IO)

    fun startRefreshData(){
        scope.launch {
            startRefreshDataUseCase()
        }
    }
}