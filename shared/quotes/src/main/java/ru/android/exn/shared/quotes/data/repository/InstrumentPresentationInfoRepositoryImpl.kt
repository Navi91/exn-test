package ru.android.exn.shared.quotes.data.repository

import io.reactivex.Single
import ru.android.exn.shared.quotes.data.datasource.InstrumentPresentationDao
import ru.android.exn.shared.quotes.data.mapper.InstrumentPresentationInfoDtoMapper
import ru.android.exn.shared.quotes.domain.entity.InstrumentPresentationInfo
import ru.android.exn.shared.quotes.domain.repository.InstrumentPresentationInfoRepository
import javax.inject.Inject

class InstrumentPresentationInfoRepositoryImpl @Inject constructor(
    private val dao: InstrumentPresentationDao,
    private val mapper: InstrumentPresentationInfoDtoMapper
) : InstrumentPresentationInfoRepository {

    override fun getInstrumentPresentationInfoList(): Single<List<InstrumentPresentationInfo>> = dao
        .getAll()
        .map { dtoList ->
            dtoList.map { dto -> mapper.toEntity(dto) }
        }
}