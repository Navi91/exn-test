package ru.android.exn.shared.quotes.data.repository

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import ru.android.exn.shared.quotes.data.datasource.QuotesSocketDataSource
import ru.android.exn.shared.quotes.data.mapper.WebSocketStateMapper
import ru.android.exn.shared.quotes.domain.entity.Instrument
import ru.android.exn.shared.quotes.domain.entity.SocketStatus
import ru.android.exn.shared.quotes.domain.repository.QuotesSocketRepository
import javax.inject.Inject

class QuotesSocketRepositoryImpl @Inject constructor(
    private val dataSource: QuotesSocketDataSource,
    private val stateMapper: WebSocketStateMapper
) : QuotesSocketRepository {

    override fun connect(): Completable = dataSource
        .connect()
        .doOnComplete { Log.d(LOG_TAG, "connect") }
        .subscribeOn(Schedulers.io())

    override fun disconnect() {
        Log.d(LOG_TAG, "disconnect")

        dataSource.disconnect()
    }

    override fun subscribe(instrument: Instrument) {
        TODO("Not yet implemented")
    }

    override fun unsubscribe(instrument: Instrument) {
        TODO("Not yet implemented")
    }

    override fun observeStatus(): Observable<SocketStatus> = dataSource
        .observeState()
        .map { state -> stateMapper.toSocketStatus(state) }
        .subscribeOn(Schedulers.io())

    private companion object {

        const val LOG_TAG = "QuotesSocketRepositoryImpl"
    }
}