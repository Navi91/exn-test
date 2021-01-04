package ru.android.exn.shared.quotes.data

import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketAdapter
import com.neovisionaries.ws.client.WebSocketState
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject
import javax.inject.Inject

class WebSocketDisconnector @Inject constructor(
    private val socket: WebSocket
) {

    private val connectionFinishedCompletable = CompletableSubject.create()

    init {
        socket.addListener(object : WebSocketAdapter() {
            override fun onStateChanged(websocket: WebSocket?, newState: WebSocketState?) {
                super.onStateChanged(websocket, newState)

                if (newState != WebSocketState.CONNECTING) {
                    connectionFinishedCompletable.onComplete()
                }
            }
        })
    }

    fun disconnect(): Completable = connectionFinishedCompletable
        .andThen(Completable.fromAction {
            socket.disconnect()
        })
}