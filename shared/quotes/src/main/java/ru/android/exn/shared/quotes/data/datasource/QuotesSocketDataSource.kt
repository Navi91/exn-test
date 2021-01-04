package ru.android.exn.shared.quotes.data.datasource

import android.util.Log
import com.neovisionaries.ws.client.WebSocketState
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import ru.android.exn.shared.quotes.data.QuotesSocket
import ru.android.exn.shared.quotes.data.QuotesWebSocketFactory
import javax.inject.Inject

class QuotesSocketDataSource @Inject constructor(
    private val factory: QuotesWebSocketFactory
) {
    private val messagesSubject = PublishSubject.create<String>()
    private val stateSubject = BehaviorSubject.create<WebSocketState>()
    private val disconnectSubject = PublishSubject.create<Unit>()

    private var activeSocket: QuotesSocket? = null

    private var compositeDisposable = CompositeDisposable()

    fun connect(): Completable {
        Log.d(LOG_TAG, "connect")

        val socket = QuotesSocket(factory)
        observeMessage(socket)
        observeState(socket)
        observeDisconnect(socket)

        activeSocket = socket

        return socket.connect()
    }

    fun disconnect() {
        Log.d(LOG_TAG, "disconnect")

        activeSocket?.disconnect()
        compositeDisposable.clear()
    }

    fun sendCommand(text: String) {
        activeSocket?.sendCommand(text)
    }

    fun observeState(): Observable<WebSocketState> =
        stateSubject.hide()

    fun observeMessage(): Observable<String> =
        messagesSubject.hide()

    fun observeDisconnect(): Observable<Unit> =
        disconnectSubject.hide()

    private fun observeMessage(socket: QuotesSocket) {
        compositeDisposable += socket.observeMessage()
            .subscribeBy(
                onNext = { message -> messagesSubject.onNext(message) },
                onError = { error ->
                    Log.e(LOG_TAG, "Observe message error: $error")
                }
            )
    }

    private fun observeState(socket: QuotesSocket) {
        compositeDisposable += socket.observeState()
            .subscribeBy(
                onNext = { state -> stateSubject.onNext(state) },
                onError = { error ->
                    Log.e(LOG_TAG, "Observe state error: $error")
                }
            )
    }

    private fun observeDisconnect(socket: QuotesSocket) {
        compositeDisposable += socket.observeDisconnect()
            .subscribeBy(
                onNext = { disconnectSubject.onNext(Unit) },
                onError = { error ->
                    Log.e(LOG_TAG, "Observe disconnect error: $error")
                }
            )
    }

    private companion object {

        const val LOG_TAG = "QuotesSocketDataSource"
    }
}