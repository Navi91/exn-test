package ru.android.exn.shared.quotes.domain.usecase

import io.reactivex.Observable
import ru.android.exn.shared.quotes.domain.entity.Instrument
import ru.android.exn.shared.quotes.domain.repository.InstrumentRepository
import javax.inject.Inject

class ObserveInstrumentsUseCase @Inject constructor(
    private val repository: InstrumentRepository
) {

    operator fun invoke(): Observable<List<Instrument>> = repository
        .observeInstruments()
}