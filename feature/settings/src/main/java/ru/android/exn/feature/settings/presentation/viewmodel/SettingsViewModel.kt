package ru.android.exn.feature.settings.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import ru.android.exn.feature.settings.presentation.mapper.InstrumentModelMapper
import ru.android.exn.feature.settings.presentation.model.InstrumentModel
import ru.android.exn.feature.settings.presentation.navigation.SettingsRouter
import ru.android.exn.shared.quotes.domain.usecase.GetInstrumentsUseCase
import ru.android.exn.shared.quotes.domain.usecase.SetInstrumentVisibilityUseCase
import javax.inject.Inject

internal class SettingsViewModel @Inject constructor(
    private val router: SettingsRouter,
    getInstrumentsUseCase: GetInstrumentsUseCase,
    private val setInstrumentVisibilityUseCase: SetInstrumentVisibilityUseCase,
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

    fun processInstrumentModelClick(model: InstrumentModel) {
        Log.d(LOG_TAG, "processInstrumentModelClick model: $model")

        compositeDisposable += setInstrumentVisibilityUseCase(model.id, !model.isChecked)
            .subscribeBy(
                onComplete = {
                    Log.d(LOG_TAG, "Set visibility completed for model: $model")

                    updateModel(model, !model.isChecked)
                },
                onError = { error ->
                    Log.e(LOG_TAG, "Set instrument visibility error: $error for model: $model")
                }
            )
    }

    fun back() {
        Log.d(LOG_TAG, "back")

        router.back()
    }

    private fun updateModel(changedModel: InstrumentModel, isChecked: Boolean) {
        val newModels = instrumentModels.value.orEmpty().map { model ->
            if (model.id == changedModel.id) {
                model.copy(isChecked = isChecked)
            } else {
                model
            }
        }

        instrumentModels.postValue(newModels)
    }

    private companion object {

        const val LOG_TAG = "SettingsViewModel"
    }
}