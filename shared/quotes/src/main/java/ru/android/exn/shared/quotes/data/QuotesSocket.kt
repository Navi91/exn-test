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

    init {
        initSocket()
    }

    fun connect(): Completable = Completable
        .fromAction {
            socket.connect()
        }
        .doOnComplete {
            messageSubject.onNext("""{"ticks":[{"s":"BTCUSD","b":"27271.04","bf":1,"a":"27286.39","af":2,"spr":"153.5"},{"s":"BTCUSD","b":"27265.07","bf":2,"a":"27285.35","af":2,"spr":"202.8"}]}""")
        }

    fun disconnect() {
        socket.removeListener(this)

        stateSubject
            .filter { webSocketState ->
                webSocketState != WebSocketState.CONNECTING
            }
            .firstElement()
            .doOnSuccess { socket.disconnect() }
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
        socket.sendText(command)
    }

    fun observeMessage(): Observable<String> =
        messageSubject

    fun observeState(): Observable<WebSocketState> =
        stateSubject

    override fun onStateChanged(websocket: WebSocket?, newState: WebSocketState) {
        Log.d(LOG_TAG, "onStateChanged newState: $newState")

        stateSubject.onNext(newState)
    }

    override fun onConnected(
        websocket: WebSocket?,
        headers: MutableMap<String, MutableList<String>>?
    ) {
        Log.d(LOG_TAG, "onConnected")

        listOf("BTCUSD", "EURUSD", "EURGBP", "USDJPY", "GBPUSD", "USDCHF", "USDCAD")
            .forEach {
                websocket?.sendText("SUBSCRIBE: $it")
            }
    }

    override fun onDisconnected(
        websocket: WebSocket?,
        serverCloseFrame: WebSocketFrame?,
        clientCloseFrame: WebSocketFrame?,
        closedByServer: Boolean
    ) {
        Log.d(LOG_TAG, "onDisconnected closedByServer: $closedByServer")

        reconnect()
    }

    override fun onTextMessageError(
        websocket: WebSocket?,
        cause: WebSocketException?,
        data: ByteArray?
    ) {
        Log.e(LOG_TAG, "onTextMessageError cause: $cause")
    }

    private fun reconnect() {
        Log.d(LOG_TAG, "reconnect")

        socket.removeListener(this)
        initSocket()
        connect()
    }

    private fun initSocket() {
        Log.d(LOG_TAG, "initSocket")

        socket = factory.create()
        socket.addListener(this)
    }

    private companion object {

        const val LOG_TAG = "QuotesSocket"
    }
}