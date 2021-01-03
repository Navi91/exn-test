package ru.android.exn.test.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.rxkotlin.subscribeBy
import ru.android.exn.shared.quotes.domain.usecase.PrepopulateInstrumentsIfNeedUseCase
import ru.android.exn.test.presentation.navigation.MainActivityRouter
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    prepopulateInstrumentsIfNeedUseCase: PrepopulateInstrumentsIfNeedUseCase,
    router: MainActivityRouter
) : ViewModel() {

    init {
        prepopulateInstrumentsIfNeedUseCase()
            .subscribeBy(
                onComplete = {
                    Log.d(LOG_TAG, "Prepopulate instruments completed")
                    router.openStartScreen()
                },
                onError = { error ->
                    Log.e(LOG_TAG, "Prepopulate instruments error: $error")
                }
            )
    }

    private companion object {
        const val LOG_TAG = "MainActivityViewModel"
    }
}