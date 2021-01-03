package ru.android.exn.shared.quotes.data.repository

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.android.exn.shared.quotes.data.datasource.InstrumentDao
import ru.android.exn.shared.quotes.data.factory.PrepopulateInstrumentFactory
import ru.android.exn.shared.quotes.data.mapper.InstrumentDtoMapper
import ru.android.exn.shared.quotes.domain.entity.Instrument
import ru.android.exn.shared.quotes.domain.repository.InstrumentRepository
import javax.inject.Inject

class InstrumentRepositoryImpl @Inject constructor(
    private val dao: InstrumentDao,
    private val mapper: InstrumentDtoMapper,
    private val factory: PrepopulateInstrumentFactory
) : InstrumentRepository {

    override fun getInstruments(): Single<List<Instrument>> = dao
        .getAll()
        .map { dtoList ->
            dtoList.map { dto -> mapper.toInstrument(dto) }
        }
        .subscribeOn(Schedulers.io())

    override fun observeInstruments(): Observable<List<Instrument>> = dao
        .observeAll()
        .map { dtoList ->
            dtoList.map { dto -> mapper.toInstrument(dto) }
        }
        .subscribeOn(Schedulers.io())

    override fun setInstrumentVisibility(
        instrumentId: String,
        isVisible: Boolean
    ): Completable = dao
        .update(instrumentId, isVisible)
        .subscribeOn(Schedulers.io())

    override fun prepopulateIfNeed(): Completable = dao
        .getAll()
        .flatMapCompletable { instruments ->
            if (instruments.isEmpty()) {
                Log.d(LOG_TAG, "Instruments is empty, start populate")

                dao.insertAll(factory.createPrepopulateInstruments())
            } else {
                Log.d(LOG_TAG, "Instruments is not empty")

                Completable.complete()
            }
        }
        .subscribeOn(Schedulers.io())

    private companion object {

        const val LOG_TAG = "InstrumentRepositoryImpl"
    }
}