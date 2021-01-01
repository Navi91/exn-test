package ru.android.exn.shared.quotes.data.datasource

import android.util.Log
import com.neovisionaries.ws.client.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import ru.android.exn.basic.dagger.ApplicationScope
import ru.android.exn.shared.quotes.data.NaiveSSLContext
import javax.inject.Inject

@ApplicationScope
class QuotesSocket @Inject constructor(){

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

    fun connect() {
        Completable.fromAction {
            socket.connect()
        }
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onComplete = {
                    Log.d(LOG_TAG, "Connect completed")
                },
                onError = { error ->
                    Log.e(LOG_TAG, "Connect to socket error: $error")
                }
            )
    }

    fun disconnect() {
        socket.disconnect()
    }

    fun observeMessages(): Observable<String> =
        messageSubject.hide()

    fun observeState(): Observable<WebSocketState> =
        stateSubject

    private companion object {

        const val LOG_TAG = "QuotesSocket"
    }
}