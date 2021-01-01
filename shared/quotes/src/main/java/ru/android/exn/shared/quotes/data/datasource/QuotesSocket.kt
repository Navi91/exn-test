package ru.android.exn.shared.quotes.data.datasource

import android.util.Log
import com.neovisionaries.ws.client.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import ru.android.exn.shared.quotes.data.NaiveSSLContext

class QuotesSocket {

    private val messageSubject = BehaviorSubject.create<String>()
    private val stateSubject = BehaviorSubject.createDefault(WebSocketState.CREATED)

    private val socket: WebSocket by lazy {
        WebSocketFactory()
            .setSSLContext(NaiveSSLContext.getInstance("TLS"))
            .setVerifyHostname(false)
            .createSocket("wss://quotes.eccalls.mobi:18400").apply {
                addListener(object : WebSocketAdapter() {

                    override fun onStateChanged(websocket: WebSocket?, newState: WebSocketState) {
                        super.onStateChanged(websocket, newState)

                        Log.d(LOG_TAG, "onStateChanged newState: $newState")

                        stateSubject.onNext(newState)
                    }

                    override fun onConnected(
                        websocket: WebSocket?,
                        headers: MutableMap<String, MutableList<String>>?
                    ) {
                        super.onConnected(websocket, headers)

                        Log.d(LOG_TAG, "onConnected")

                        listOf("BTCUSD", "EURUSD", "EURGBP", "USDJPY", "GBPUSD", "USDCHF", "USDCAD")
                            .forEach {
                                websocket?.sendText("SUBSCRIBE: $it")
                            }

                    }

                    override fun onTextMessageError(
                        websocket: WebSocket?,
                        cause: WebSocketException?,
                        data: ByteArray?
                    ) {
                        super.onTextMessageError(websocket, cause, data)

                        Log.e(LOG_TAG, "onTextMessageError cause: $cause")
                    }

                    override fun onTextMessage(websocket: WebSocket?, text: String?) {
                        super.onTextMessage(websocket, text)

                        Log.d(LOG_TAG, "onTextMessage text: $text")

                        messageSubject.onNext(text.orEmpty())
                    }

                    override fun onMessageError(
                        websocket: WebSocket?,
                        cause: WebSocketException?,
                        frames: MutableList<WebSocketFrame>?
                    ) {
                        super.onMessageError(websocket, cause, frames)


                        Log.d(LOG_TAG, "onMessageError cause: $cause")
                    }
                })
            }
    }

    init {
        messageSubject.onNext("""{"ticks":[{"s":"BTCUSD","b":"27271.04","bf":1,"a":"27286.39","af":2,"spr":"153.5"},{"s":"BTCUSD","b":"27265.07","bf":2,"a":"27285.35","af":2,"spr":"202.8"}]}""")
    }

    fun connect(): Completable = Completable
        .fromAction {
            socket.connect()
        }

    fun disconnect() {
        socket.disconnect(WebSocketCloseCode.AWAY)
    }

    fun observeMessages(): Observable<String> =
        messageSubject.hide()

    fun observeState(): Observable<WebSocketState> =
        stateSubject

    private companion object {

        const val LOG_TAG = "QuotesSocket"
    }
}