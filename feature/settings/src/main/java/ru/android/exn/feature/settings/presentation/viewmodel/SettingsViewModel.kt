package ru.android.exn.feature.settings.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.plusAssign
import ru.android.exn.feature.settings.presentation.navigation.SettingsRouter
import ru.android.exn.shared.quotes.domain.usecase.GetInstrumentsUseCase
import javax.inject.Inject

internal class SettingsViewModel @Inject constructor(
    private val router: SettingsRouter,
    getInstrumentsUseCase: GetInstrumentsUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable += getInstrumentsUseCase.invoke()
            .subscribeBy(
                onSuccess = { instruments ->
                    Log.d(LOG_TAG, "Get instruments success: $instruments")
                },
                onError = { error ->
                    Log.e(LOG_TAG, "Get instruments error: $error")
                }
            )
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.dispose()
    }

    fun back() {
        Log.d(LOG_TAG, "back")

        router.back()
    }

    private companion object {

        const val LOG_TAG = "SettingsViewModel"
    }
}