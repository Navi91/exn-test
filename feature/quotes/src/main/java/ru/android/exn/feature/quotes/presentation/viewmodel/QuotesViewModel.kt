package ru.android.exn.feature.quotes.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import ru.android.exn.feature.quotes.presentation.navigation.QuotesRouter
import ru.android.exn.shared.quotes.domain.entity.Quote
import ru.android.exn.shared.quotes.domain.entity.SocketStatus
import ru.android.exn.shared.quotes.domain.interactor.QuotesSocketInteractor
import ru.android.exn.shared.quotes.domain.usecase.GetInstrumentsUseCase
import ru.android.exn.shared.quotes.domain.usecase.ObserveQuotesUseCase
import javax.inject.Inject

internal class QuotesViewModel @Inject constructor(
    private val router: QuotesRouter,
    private val interactor: QuotesSocketInteractor,
    private val observeQuotesUseCase: ObserveQuotesUseCase,
    private val getInstrumentsUseCase: GetInstrumentsUseCase
) : ViewModel() {

    val socketStatus = MutableLiveData<SocketStatus>()
    val model = MutableLiveData<List<Quote>>()

    private val quotes = mutableListOf<Quote>()
    private val instrumentsOrderList = mutableListOf<String>()

    private val compositeDisposable = CompositeDisposable()

    init {
        observeSocketStatus()

        getInstrumentsUseCase()
            .map { instruments -> instruments.filter { it.isSubscribed } }
            .map { instruments -> instruments.map { it.id } }
            .doOnSuccess { instrumentIds ->
                Log.d(LOG_TAG, "Get instruments success: $instrumentIds")

                instrumentsOrderList.addAll(instrumentIds)
            }
            .flatMapObservable { observeQuotesUseCase() }
            .subscribeBy(
                onNext = { quotes ->
                    Log.v(LOG_TAG, "New quotes: $quotes")

                    setQuotes(quotes)
                },
                onError = { error ->
                    Log.e(LOG_TAG, "Observe quotes error: $error")
                }
            )
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.dispose()
    }

    fun processStart() {
        Log.d(LOG_TAG, "processStart")

        compositeDisposable += interactor.connect()
            .subscribeBy(
                onComplete = {
                    Log.d(LOG_TAG, "Connection completed")
                },
                onError = { error ->
                    Log.e(LOG_TAG, "Connect error: $error")
                }
            )
    }

    fun processStop() {
        Log.d(LOG_TAG, "processStop")

        interactor.disconnect()
    }

    fun openSettings() {
        Log.d(LOG_TAG, "openSettings")

        router.openSettings()
    }

    fun back() {
        Log.d(LOG_TAG, "back")

        router.back()
    }

    private fun observeSocketStatus() {
        compositeDisposable += interactor.observeStatus()
            .subscribe({ status ->
                Log.d(LOG_TAG, "New socket status: $status")

                socketStatus.postValue(status)
            }, { error ->
                Log.e(LOG_TAG, "Observe socket status error: $error")
            })
    }

    private fun setQuotes(quotes: List<Quote>) {
        this.quotes.apply {
            clear()
            addAll(quotes)
        }

        updateModel()
    }

    private fun updateModel() {
        val modelQuotes = mutableListOf<Quote>()

        instrumentsOrderList.forEach { instrumentId ->
            val quote = quotes.firstOrNull { it.instrumentId == instrumentId }

            if (quote != null) {
                modelQuotes.add(quote)
            }
        }

        Log.d(LOG_TAG, "Post model: $modelQuotes")

        model.postValue(modelQuotes)
    }

    private companion object {

        const val LOG_TAG = "QuotesViewModel"
    }
}