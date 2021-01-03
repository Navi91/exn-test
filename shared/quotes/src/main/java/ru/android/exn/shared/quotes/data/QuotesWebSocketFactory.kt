package ru.android.exn.shared.quotes.data

import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketFactory
import javax.inject.Inject

class QuotesWebSocketFactory @Inject constructor() {

    fun create(): WebSocket =
        WebSocketFactory()
            .setConnectionTimeout(5000)
            .setSSLContext(NaiveSSLContext.getInstance("TLS"))
            .setVerifyHostname(false)
            .createSocket("wss://quotes.eccalls.mobi:18400")
}