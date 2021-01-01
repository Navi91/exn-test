package ru.android.exn.shared.quotes.domain.repository

import io.reactivex.Completable
import io.reactivex.Observable
import ru.android.exn.shared.quotes.domain.entity.SocketStatus

interface QuotesSocketRepository {

    fun connect(): Completable

    fun disconnect()

    fun observeStatus(): Observable<SocketStatus>
}