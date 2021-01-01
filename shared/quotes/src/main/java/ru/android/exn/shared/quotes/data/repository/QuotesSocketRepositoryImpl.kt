package ru.android.exn.shared.quotes.data.repository

import android.util.Log
import io.reactivex.Observable
import ru.android.exn.shared.quotes.data.datasource.QuotesSocket
import ru.android.exn.shared.quotes.data.mapper.WebSocketStateMapper
import ru.android.exn.shared.quotes.domain.entity.SocketStatus
import ru.android.exn.shared.quotes.domain.repository.QuotesSocketRepository

class QuotesSocketRepositoryImpl(
    private val socket: QuotesSocket,
    private val stateMapper: WebSocketStateMapper
) : QuotesSocketRepository {

    override fun connect() {
        Log.d(LOG_TAG, "connect")

        socket.connect()
    }

    override fun disconnect() {
        Log.d(LOG_TAG, "disconnect")

        socket.disconnect()
    }

    override fun observeStatus(): Observable<SocketStatus> = socket
        .observeState()
        .map { state -> stateMapper.toSocketStatus(state) }

    private companion object {

        const val LOG_TAG = "QuotesSocketRepositoryImpl"
    }
}