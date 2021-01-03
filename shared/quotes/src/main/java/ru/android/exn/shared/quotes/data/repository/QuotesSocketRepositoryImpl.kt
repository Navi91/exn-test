package ru.android.exn.shared.quotes.data.repository

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.android.exn.shared.quotes.data.datasource.InstrumentDao
import ru.android.exn.shared.quotes.data.datasource.QuotesSocketDataSource
import ru.android.exn.shared.quotes.data.mapper.InstrumentDtoMapper
import ru.android.exn.shared.quotes.data.mapper.SocketCommandMapper
import ru.android.exn.shared.quotes.data.mapper.WebSocketStateMapper
import ru.android.exn.shared.quotes.domain.entity.SocketStatus
import ru.android.exn.shared.quotes.domain.repository.QuotesSocketRepository
import javax.inject.Inject

class QuotesSocketRepositoryImpl @Inject constructor(
    private val dataSource: QuotesSocketDataSource,
    private val instrumentDao: InstrumentDao,
    private val stateMapper: WebSocketStateMapper,
    private val socketCommandMapper: SocketCommandMapper,
    private val instrumentDtoMapper: InstrumentDtoMapper
) : QuotesSocketRepository {

    override fun connect(): Completable = dataSource
        .connect()
        .doOnComplete { Log.d(LOG_TAG, "Connection connect") }
        .andThen(instrumentDao.getAll())
        .map { instrumentDtoList ->
            instrumentDtoList.filter { instrumentDto ->
                instrumentDto.isSubscribed
            }
        }
        .map { instrumentDtoList ->
            instrumentDtoList.map { instrumentDto ->
                instrumentDtoMapper.toInstrument(instrumentDto)
            }
        }
        .flattenAsFlowable { it }
        .flatMapCompletable { instrument -> subscribe(instrument.id) }
        .doOnComplete { Log.d(LOG_TAG, "Send subscribe commands completed") }
        .subscribeOn(Schedulers.io())

    override fun disconnect() {
        Log.d(LOG_TAG, "disconnect")

        dataSource.disconnect()
    }

    override fun subscribe(instrumentId: String): Completable = Single
        .fromCallable {
            socketCommandMapper.toSubscribeCommand(instrumentId)
        }
        .doOnSubscribe { Log.d(LOG_TAG, "subscribe instrument: $instrumentId") }
        .doOnSuccess { subscribeCommand ->
            dataSource.sendCommand(subscribeCommand)
        }
        .ignoreElement()

    override fun unsubscribe(instrumentId: String): Completable = Single
        .fromCallable {
            socketCommandMapper.toUnsubscribeCommand(instrumentId)
        }
        .doOnSubscribe { Log.d(LOG_TAG, "unsubscribe instrument: $instrumentId") }
        .doOnSuccess { subscribeCommand ->
            dataSource.sendCommand(subscribeCommand)
        }
        .ignoreElement()

    override fun observeStatus(): Observable<SocketStatus> = dataSource
        .observeState()
        .map { state -> stateMapper.toSocketStatus(state) }
        .subscribeOn(Schedulers.io())

    private companion object {

        const val LOG_TAG = "QuotesSocketRepositoryImpl"
    }
}