package ru.android.exn.feature.splah.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.rxkotlin.subscribeBy
import ru.android.exn.feature.splah.presentation.navigation.SplashRouter
import ru.android.exn.shared.quotes.domain.usecase.PrepopulateInstrumentsIfNeedUseCase
import javax.inject.Inject

internal class SplashViewModel @Inject constructor(
    router: SplashRouter,
    prepopulateInstrumentsIfNeedUseCase: PrepopulateInstrumentsIfNeedUseCase
) : ViewModel(){

    init {
        prepopulateInstrumentsIfNeedUseCase()
            .subscribeBy(
                onComplete = {
                    Log.d(LOG_TAG, "Prepopulate instruments completed")
                    router.openQuotesScreen()
                },
                onError = { error ->
                    Log.e(LOG_TAG, "Prepopulate instruments error: $error")
                }
            )
    }

    private companion object {
        const val LOG_TAG = "SplashViewModel"
    }
}