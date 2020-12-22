package ru.android.exn.shared.quotes.domain.repository

import io.reactivex.Single
import ru.android.exn.shared.quotes.domain.entity.InstrumentPresentationInfo

interface InstrumentPresentationInfoRepository {

    fun getInstrumentPresentationInfoList(): Single<List<InstrumentPresentationInfo>>
}