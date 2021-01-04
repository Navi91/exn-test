package ru.android.exn.feature.quotes.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import ru.android.exn.feature.quotes.presentation.mapper.QuoteModelMapper
import ru.android.exn.feature.quotes.presentation.model.QuoteModel
import ru.android.exn.feature.quotes.presentation.model.SocketStatusModel
import ru.android.exn.feature.quotes.presentation.navigation.QuotesRouter
import ru.android.exn.shared.quotes.domain.entity.Instrument
import ru.android.exn.shared.quotes.domain.entity.Quote
import ru.android.exn.shared.quotes.domain.interactor.QuotesSocketInteractor
import ru.android.exn.shared.quotes.domain.usecase.ObserveInstrumentsUseCase
import ru.android.exn.shared.quotes.domain.usecase.ObserveQuotesUseCase
import ru.android.exn.shared.quotes.domain.usecase.SetInstrumentSubscriptionUseCase
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class QuotesViewModel @Inject constructor(
    private val router: QuotesRouter,
    private val interactor: QuotesSocketInteractor,
    private val observeQuotesUseCase: ObserveQuotesUseCase,
    private val observeInstrumentsUseCase: ObserveInstrumentsUseCase,
    private val setInstrumentSubscriptionUseCase: SetInstrumentSubscriptionUseCase,
    private val quoteModelMapper: QuoteModelMapper
) : ViewModel() {

    private val socketStatusMutable = MutableLiveData<SocketStatusModel>()
    val socketStatus = Transformations.distinctUntilChanged(socketStatusMutable)
    val model = MutableLiveData<List<QuoteModel>>()

    private val quotes = mutableListOf<Quote>()
    private val orderInstruments = mutableListOf<Instrument>()

    private val isStartedSubject = PublishSubject.create<Boolean>()

    private val compositeDisposable = CompositeDisposable()

    init {
        observeQuotes()
        observeInstruments()
        observeSocketConnection()
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.dispose()
    }

    fun processStart() {
        Log.d(LOG_TAG, "processStart")

        isStartedSubject.onNext(true)
    }

    fun processStop() {
        Log.d(LOG_TAG, "processStop")

        isStartedSubject.onNext(false)
    }

    fun processMove(fromPosition: Int, newPosition: Int) {
        Log.d(LOG_TAG, "processMove fromPosition: $fromPosition newPosition: $newPosition")

        Completable.fromAction {
            val fromInstrument = getInstrument(fromPosition)
            val currentInstrumentOnNewPosition = getInstrument(newPosition)

            val newIndex = orderInstruments.indexOfLast {
                it.id == currentInstrumentOnNewPosition.id
            }

            orderInstruments.remove(fromInstrument)
            orderInstruments.add(newIndex, fromInstrument)

            updateModel()
        }
            .subscribeBy(
                onComplete = {
                    Log.d(LOG_TAG, "Move completed")
                },
                onError = { error ->
                    Log.e(LOG_TAG, "Move error: $error")
                }
            )
    }

    fun processSwipe(swipedPosition: Int) {
        Log.d(LOG_TAG, "processSwipe swipedPosition: $swipedPosition")

        Single.fromCallable {
            getInstrument(swipedPosition)
        }
            .doOnSuccess { instrument -> hideInstrument(instrument) }
            .flatMapCompletable { instrument ->
                setInstrumentSubscriptionUseCase(instrument.id, false)
            }
            .subscribeBy(
                onComplete = {
                    Log.d(LOG_TAG, "Process instrument swipe completed")
                },
                onError = { error ->
                    Log.e(LOG_TAG, "Process instrument swipe error: $error")
                }
            )
    }

    fun openSettings() {
        Log.d(LOG_TAG, "openSettings")

        router.openSettings()
    }

    fun back() {
        Log.d(LOG_TAG, "back")

        router.back()
    }

    private fun observeSocketConnection() {
        compositeDisposable += isStartedSubject
            .switchMapSingle { isStarted ->
                if (isStarted) {
                    Single.just(true)
                } else {
                    Single.just(false)
                        .delay(DISCONNECT_AFTER_STOP_DURATION_SEC, TimeUnit.SECONDS)
                }
            }
            .distinctUntilChanged()
            .switchMapCompletable { isStarted ->
                Log.d(LOG_TAG, "Process isStarted: $isStarted")

                if (isStarted) {
                    interactor.connect()
                        .doOnSubscribe {
                            Log.d(LOG_TAG, "Try to connect")

                            socketStatusMutable.postValue(SocketStatusModel.CONNECTING)
                        }
                        .doOnComplete {
                            socketStatusMutable.postValue(SocketStatusModel.CONNECTED)
                        }
                        .doOnError { error ->
                            Log.w(LOG_TAG, "Connect error: $error")
                        }
                        .retryWhen { handler ->
                            handler
                                .flatMap {
                                    Flowable.timer(
                                        RECONNECT_DURATION_SEC,
                                        TimeUnit.SECONDS
                                    )
                                }
                        }
                        .repeatWhen { handler ->
                            handler
                                .flatMapMaybe { interactor.observeDisconnect().firstElement() }
                        }
                } else {
                    Completable.fromAction { interactor.disconnect() }
                }
            }
            .subscribeBy(
                onComplete = {
                    Log.d(LOG_TAG, "Observe start completed")
                },
                onError = { error ->
                    Log.e(LOG_TAG, "Observe start error: $error")
                }
            )
    }

    private fun observeQuotes() {
        observeQuotesUseCase()
            .observeOn(AndroidSchedulers.mainThread())
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
        compositeDisposable += observeInstrumentsUseCase()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { instruments ->
                    Log.d(LOG_TAG, "New subscribed instruments success: $instruments")

                    updateInstruments(instruments)
                },
                onError = { error ->
                    Log.e(LOG_TAG, "Observe instruments error: $error")
                }
            )
    }

    private fun updateInstruments(updateInstruments: List<Instrument>) {
        Log.d(LOG_TAG, "updateInstruments updateInstruments: $updateInstruments")

        val newOrderInstruments = mutableListOf<Instrument>()

        updateInstruments.forEach { instrument ->
            if (orderInstruments.none { it.id == instrument.id }) {
                orderInstruments.add(instrument)
            }
        }

        orderInstruments.forEach { instrument ->
            newOrderInstruments.add(updateInstruments.first { it.id == instrument.id })
        }

        orderInstruments.clear()
        orderInstruments.addAll(newOrderInstruments)

        updateModel()
    }

    private fun hideInstrument(instrument: Instrument) {
        orderInstruments.replaceInstrumentById(instrument.copy(isSubscribed = false))

        updateModel()
    }

    private fun MutableList<Instrument>.replaceInstrumentById(instrument: Instrument) {
        val index = indexOfFirst { it.id == instrument.id }

        if (index != -1) {
            set(index, instrument)
        }
    }

    private fun updateQuotes(quotes: List<Quote>) {
        this.quotes.apply {
            clear()
            addAll(quotes)
        }

        updateModel()
    }

    private fun updateModel() {
        val quoteModels = orderInstruments
            .filter { it.isSubscribed }
            .map { instrument ->
                val quote = quotes.firstOrNull { it.instrumentId == instrument.id }

                quoteModelMapper.toQuoteModel(instrument, quote)
            }

        Log.v(LOG_TAG, "Post quote model: $quoteModels")

        model.postValue(quoteModels)
    }

    private fun getInstrument(position: Int): Instrument {
        var index = 0

        orderInstruments.forEach { instrument ->
            if (instrument.isSubscribed && index == position) {
                return instrument
            }

            if (instrument.isSubscribed) index++
        }

        error("Not found visible instrument for position: $position and order instruments: $orderInstruments")
    }

    private companion object {

        const val LOG_TAG = "QuotesViewModel"

        const val RECONNECT_DURATION_SEC = 1L
        const val DISCONNECT_AFTER_STOP_DURATION_SEC = 2L
    }
}