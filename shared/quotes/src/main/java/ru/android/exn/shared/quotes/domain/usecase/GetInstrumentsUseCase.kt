package ru.android.exn.shared.quotes.domain.usecase

import io.reactivex.Single
import ru.android.exn.shared.quotes.domain.entity.Instrument
import ru.android.exn.shared.quotes.domain.repository.InstrumentRepository
import javax.inject.Inject

class GetInstrumentsUseCase @Inject constructor(
    private val repository: InstrumentRepository
){

    operator fun invoke(): Single<List<Instrument>> =
        repository.getInstruments()
}