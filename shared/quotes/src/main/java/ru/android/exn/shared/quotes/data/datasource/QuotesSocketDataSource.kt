package ru.android.exn.shared.quotes.data.datasource

import com.neovisionaries.ws.client.WebSocketState
import io.reactivex.Completable
import io.reactivex.Observable
import ru.android.exn.shared.quotes.data.QuotesSocket
import ru.android.exn.shared.quotes.data.QuotesWebSocketFactory
import javax.inject.Inject

class QuotesSocketDataSource @Inject constructor(
    private val factory: QuotesWebSocketFactory
) {
    private var messagesObservable: Observable<String> = Observable.empty()
    private var stateObservable: Observable<WebSocketState> = Observable.empty()

    private var activeSocket: QuotesSocket? = null

    fun connect(): Completable {
        val socket = QuotesSocket(factory)

        activeSocket = socket
        messagesObservable = socket.observeMessage()
        stateObservable = socket.observeState()

        return socket.connect()
    }

    fun disconnect() {
        activeSocket?.disconnect()
        messagesObservable = Observable.empty()
        stateObservable = Observable.empty()
    }

    fun sendCommand(text: String) {
        activeSocket?.sendCommand(text)
    }

    fun observeState(): Observable<WebSocketState> =
        stateObservable

    fun observeMessages(): Observable<String> =
        messagesObservable
}