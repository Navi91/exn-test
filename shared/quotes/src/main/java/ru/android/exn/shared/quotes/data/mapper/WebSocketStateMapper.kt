package ru.android.exn.shared.quotes.data.mapper

import com.neovisionaries.ws.client.WebSocketState
import ru.android.exn.shared.quotes.domain.entity.SocketStatus
import javax.inject.Inject

class WebSocketStateMapper @Inject constructor() {

    fun toSocketStatus(state: WebSocketState): SocketStatus =
        when (state) {
            WebSocketState.CREATED -> SocketStatus.CREATED
            WebSocketState.CONNECTING -> SocketStatus.CONNECTING
            WebSocketState.OPEN -> SocketStatus.OPEN
            WebSocketState.CLOSING -> SocketStatus.CLOSING
            WebSocketState.CLOSED -> SocketStatus.CLOSED
        }
}