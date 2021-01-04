package ru.android.exn.shared.quotes.data

import android.util.Log
import com.neovisionaries.ws.client.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class QuotesSocket(
    private val factory: QuotesWebSocketFactory
) : WebSocketListener by WebSocketAdapter() {

    private lateinit var socket: WebSocket

    private val messageSubject = PublishSubject.create<String>()
    private val stateSubject = BehaviorSubject.create<WebSocketState>()
    private val disconnectSubject = PublishSubject.create<Unit>()

    init {
        initSocket()
    }

    fun connect(): Completable = Completable
        .fromAction {
            socket.connect()
        }
        .andThen(stateSubject)
        .filter { webSocketState -> webSocketState == WebSocketState.OPEN }
        .firstElement()
        .ignoreElement()

    fun disconnect() {
        socket.removeListener(this)

        WebSocketDisconnector(socket)
            .disconnect()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onComplete = {
                    Log.d(LOG_TAG, "Socket disconnect completed")
                },
                onError = { error ->
                    Log.e(LOG_TAG, "Socket disconnect error: $error")
                }
            )
    }

    fun sendCommand(command: String) {
        Log.d(LOG_TAG, "sendCommand command: $command")

        socket.sendText(command)
    }

    override fun onStateChanged(websocket: WebSocket?, newState: WebSocketState) {
        Log.d(LOG_TAG, "onStateChanged newState: $newState")

        stateSubject.onNext(newState)
    }

    override fun onDisconnected(
        websocket: WebSocket?,
        serverCloseFrame: WebSocketFrame?,
        clientCloseFrame: WebSocketFrame?,
        closedByServer: Boolean
    ) {
        Log.d(LOG_TAG, "onDisconnected closedByServer: $closedByServer")

        disconnectSubject.onNext(Unit)
    }

    override fun onTextMessage(websocket: WebSocket?, text: String?) {
        Log.d(LOG_TAG, "onTextMessage text: $text")

        messageSubject.onNext(text.orEmpty())
    }

    private fun initSocket() {
        Log.d(LOG_TAG, "initSocket")

        socket = factory.create()
        socket.addListener(this)
    }

    fun observeMessage(): Observable<String> =
        messageSubject

    fun observeState(): Observable<WebSocketState> =
        stateSubject

    fun observeDisconnect(): Observable<Unit> =
        disconnectSubject

    private companion object {

        const val LOG_TAG = "QuotesSocket"
    }
}