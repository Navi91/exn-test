package ru.android.exn.feature.settings.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.plusAssign
import ru.android.exn.feature.settings.presentation.mapper.InstrumentModelMapper
import ru.android.exn.feature.settings.presentation.model.InstrumentModel
import ru.android.exn.feature.settings.presentation.navigation.SettingsRouter
import ru.android.exn.shared.quotes.domain.usecase.GetInstrumentsUseCase
import javax.inject.Inject

internal class SettingsViewModel @Inject constructor(
    private val router: SettingsRouter,
    getInstrumentsUseCase: GetInstrumentsUseCase,
    private val instrumentModelMapper: InstrumentModelMapper
) : ViewModel() {

    val instrumentModels = MutableLiveData<List<InstrumentModel>>()

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable += getInstrumentsUseCase.invoke()
            .map { instruments ->
                instruments.map { instrument ->
                    instrumentModelMapper.toInstrumentModel(instrument)
                }
            }
            .subscribeBy(
                onSuccess = { instrumentModels ->
                    Log.d(LOG_TAG, "Get instruments success: $instrumentModels")

                    this.instrumentModels.postValue(instrumentModels)
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