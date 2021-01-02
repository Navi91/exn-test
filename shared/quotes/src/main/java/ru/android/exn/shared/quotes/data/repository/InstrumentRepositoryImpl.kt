package ru.android.exn.shared.quotes.data.repository

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.android.exn.shared.quotes.data.datasource.InstrumentDao
import ru.android.exn.shared.quotes.data.mapper.InstrumentMapper
import ru.android.exn.shared.quotes.domain.entity.Instrument
import ru.android.exn.shared.quotes.domain.repository.InstrumentRepository
import javax.inject.Inject

class InstrumentRepositoryImpl @Inject constructor(
    private val dao: InstrumentDao,
    private val mapper: InstrumentMapper
) : InstrumentRepository {

    override fun getInstruments(): Single<List<Instrument>> = dao
        .getAllSingle()
        .map { dtoList ->
            dtoList.map { dto -> mapper.toInstrument(dto) }
        }
        .subscribeOn(Schedulers.io())
}