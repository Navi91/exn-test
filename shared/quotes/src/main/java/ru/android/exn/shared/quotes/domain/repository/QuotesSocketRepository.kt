package ru.android.exn.shared.quotes.domain.repository

import io.reactivex.Observable
import ru.android.exn.shared.quotes.domain.entity.SocketStatus

interface QuotesSocketRepository {

    fun connect()

    fun disconnect()

    fun observeStatus(): Observable<SocketStatus>
}