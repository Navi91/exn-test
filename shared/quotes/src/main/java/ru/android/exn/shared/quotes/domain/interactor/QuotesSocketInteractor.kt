package ru.android.exn.shared.quotes.domain.interactor

import io.reactivex.Completable
import io.reactivex.Observable
import ru.android.exn.shared.quotes.domain.repository.QuotesSocketRepository
import ru.android.exn.shared.quotes.domain.entity.SocketStatus
import ru.android.exn.shared.quotes.domain.repository.InstrumentRepository
import javax.inject.Inject

class QuotesSocketInteractor @Inject constructor(
    private val repository: QuotesSocketRepository
) {

    fun connect(): Completable =
        repository.connect()

    fun disconnect() {
        repository.disconnect()
    }

    fun observeDisconnect(): Observable<Unit> =
        repository.observeDisconnect()
}