package ru.android.exn.shared.quotes.domain.interactor

import io.reactivex.Observable
import ru.android.exn.shared.quotes.domain.repository.QuotesSocketRepository
import ru.android.exn.shared.quotes.domain.entity.SocketStatus
import javax.inject.Inject

class QuotesSocketInteractor @Inject constructor(
    private val controller: QuotesSocketRepository
) {

    fun connect() {
        controller.connect()
    }

    fun disconnect() {
        controller.disconnect()
    }

    fun observeStatus(): Observable<SocketStatus> =
        controller.observeStatus()
}