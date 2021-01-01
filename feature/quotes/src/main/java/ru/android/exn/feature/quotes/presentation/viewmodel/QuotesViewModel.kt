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
import ru.android.exn.shared.quotes.domain.usecase.ObserveQuotesUseCase
import javax.inject.Inject

internal class QuotesViewModel @Inject constructor(
        private val router: QuotesRouter,
        private val interactor: QuotesSocketInteractor,
        observeQuotesUseCase: ObserveQuotesUseCase
) : ViewModel() {

    val socketStatus = MutableLiveData<SocketStatus>()
    val quotes = MutableLiveData<List<Quote>>()

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable += interactor.connect()
                .subscribeBy(
                        onComplete = {
                            Log.d(LOG_TAG, "Connection completed")
                        },
                        onError = { error ->
                            Log.e(LOG_TAG, "Connect error: $error")
                        }
                )

        compositeDisposable += interactor.observeStatus()
                .subscribe({ status ->
                    Log.d(LOG_TAG, "New socket status: $status")

                    socketStatus.postValue(status)
                }, { error ->
                    Log.e(LOG_TAG, "Observe socket status error: $error")
                })

        compositeDisposable += observeQuotesUseCase()
                .subscribeBy(
                        onNext = { quotes ->
                            Log.v(LOG_TAG, "New quotes: $quotes")

                            this.quotes.postValue(quotes)
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

    private companion object {

        const val LOG_TAG = "QuotesViewModel"
    }
}