package ru.android.exn.feature.quotes.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import ru.android.exn.feature.quotes.presentation.navigation.QuotesRouter
import ru.android.exn.shared.quotes.domain.entity.Quote
import ru.android.exn.shared.quotes.domain.entity.SocketStatus
import ru.android.exn.shared.quotes.domain.interactor.QuotesSocketInteractor
import ru.android.exn.shared.quotes.domain.usecase.ObserveInstrumentsUseCase
import ru.android.exn.shared.quotes.domain.usecase.ObserveQuotesUseCase
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class QuotesViewModel @Inject constructor(
    private val router: QuotesRouter,
    private val interactor: QuotesSocketInteractor,
    private val observeQuotesUseCase: ObserveQuotesUseCase,
    private val observeInstrumentsUseCase: ObserveInstrumentsUseCase
) : ViewModel() {

    val socketStatus = MutableLiveData<SocketStatus>()
    val model = MutableLiveData<List<Quote>>()

    private val quotes = mutableListOf<Quote>()
    private val orderInstrumentIds = mutableListOf<String>()

    private val compositeDisposable = CompositeDisposable()

    init {
        observeSocketStatus()
        observeQuotes()
        observeInstruments()
    }

    private fun observeQuotes() {
        observeQuotesUseCase()
            .subscribeBy(
                onNext = { quotes ->
                    Log.v(LOG_TAG, "New quotes: $quotes")

                    updateQuotes(quotes)
                },
                onError = { error ->
                    Log.e(LOG_TAG, "Observe quotes error: $error")
                }
            )
    }

    private fun observeInstruments() {
        observeInstrumentsUseCase()
            .map { instruments -> instruments.filter { it.isSubscribed } }
            .map { instruments -> instruments.map { it.id } }
            .subscribeBy(
                onNext = { instrumentIds ->
                    Log.d(LOG_TAG, "New subscribed instruments success: $instrumentIds")

                    updateInstrumentIds(instrumentIds)
                },
                onError = { error ->
                    Log.e(LOG_TAG, "Observe instruments error: $error")
                }
            )
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.dispose()
    }


    private var isDisconnected = true
    private var disconnectTimerDisposable: Disposable? = null

    fun processStart() {
        Log.d(LOG_TAG, "processStart")

        if (isDisconnected) {
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

        isDisconnected = false
        disconnectTimerDisposable?.dispose()
    }


    fun processStop() {
        Log.d(LOG_TAG, "processStop")

        disconnectTimerDisposable = Observable.timer(2, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    Log.d(LOG_TAG, "Disconnect timer is finished")

                    isDisconnected = true
                    interactor.disconnect()
                },
                onError = { error ->
                    Log.e(LOG_TAG, "Disconnect timer error: $error")
                }
            )
    }

    fun processMove(fromPosition: Int, newPosition: Int) {

    }

    fun processSwipe(swipedPosition: Int) {

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

    private fun updateInstrumentIds(newSubscribedInstrumentIds: List<String>) {
        val newInstrumentIds = mutableListOf<String>()

        orderInstrumentIds.forEach { instrumentId ->
            if (newSubscribedInstrumentIds.contains(instrumentId)) {
                newInstrumentIds.add(instrumentId)
            }
        }

        newSubscribedInstrumentIds.forEach { instrumentId ->
            if (!orderInstrumentIds.contains(instrumentId)) {
                newInstrumentIds.add(instrumentId)
            }
        }

        orderInstrumentIds.apply {
            clear()
            addAll(newInstrumentIds)
        }

        updateModel()
    }

    private fun updateQuotes(quotes: List<Quote>) {
        this.quotes.apply {
            clear()
            addAll(quotes)
        }

        updateModel()
    }

    private fun updateModel() {
        val orderedQuotes = mutableListOf<Quote>()

        orderInstrumentIds.forEach { instrumentId ->
            val quote = quotes.firstOrNull { it.instrumentId == instrumentId }

            if (quote != null) {
                orderedQuotes.add(quote)
            }
        }

        model.postValue(orderedQuotes)
    }

    private companion object {

        const val LOG_TAG = "QuotesViewModel"
    }
}