package ru.android.exn.shared.quotes.data.repository

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
        .getAllSingle()
        .map { dtoList ->
            dtoList.map { dto -> mapper.toInstrument(dto) }
        }
        .subscribeOn(Schedulers.io())

    override fun observeInstruments(): Observable<List<Instrument>> {
        TODO("Not yet implemented")
    }

    override fun setInstrumentVisibility(
        instrumentId: String,
        isVisible: Boolean
    ): Completable = dao
        .update(instrumentId, isVisible)
        .subscribeOn(Schedulers.io())

    override fun prepopulateIfNeed(): Completable = dao
        .getAllSingle()
        .flatMapCompletable { instruments ->
            if (instruments.isEmpty()) {
                dao.insertAll(factory.createPrepopulateInstruments())
            } else {
                Completable.complete()
            }
        }
        .subscribeOn(Schedulers.io())
}