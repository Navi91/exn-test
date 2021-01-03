package ru.android.exn.shared.quotes.data.datasource

import android.util.Log
import com.neovisionaries.ws.client.WebSocketState
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import ru.android.exn.shared.quotes.data.QuotesSocket
import ru.android.exn.shared.quotes.data.QuotesWebSocketFactory
import javax.inject.Inject

class QuotesSocketDataSource @Inject constructor(
    private val factory: QuotesWebSocketFactory
) {
    private var messagesSubject = PublishSubject.create<String>()
    private var stateSubject = BehaviorSubject.create<WebSocketState>()

    private var activeSocket: QuotesSocket? = null

    private var messageDisposable: Disposable? = null
    private var stateDisposable: Disposable? = null

    fun connect(): Completable {
        val socket = QuotesSocket(factory)
        observeMessage(socket)
        observeState(socket)

        activeSocket = socket

        return socket.connect()
    }

    fun disconnect() {
        activeSocket?.disconnect()
        messageDisposable?.dispose()
        stateDisposable?.dispose()
    }

    fun sendCommand(text: String) {
        activeSocket?.sendCommand(text)
    }

    fun observeState(): Observable<WebSocketState> =
        stateSubject.hide()

    fun observeMessage(): Observable<String> =
        messagesSubject.hide()

    private fun observeMessage(socket: QuotesSocket) {
        messageDisposable = socket.observeMessage()
            .subscribeBy(
                onNext = { message -> messagesSubject.onNext(message) },
                onError = { error ->
                    Log.e(LOG_TAG, "Observe message error: $error")
                }
            )
    }

    private fun observeState(socket: QuotesSocket) {
        stateDisposable = socket.observeState()
            .subscribeBy(
                onNext = { state -> stateSubject.onNext(state) },
                onError = { error ->
                    Log.e(LOG_TAG, "Observe state error: $error")
                }
            )
    }

    private companion object {

        const val LOG_TAG = "QuotesSocketDataSource"
    }
}