package ru.android.exn.shared.quotes.domain.usecase

import io.reactivex.Completable
import ru.android.exn.shared.quotes.domain.repository.InstrumentRepository
import javax.inject.Inject

class SetInstrumentVisibilityUseCase @Inject constructor(
    private val repository: InstrumentRepository
) {

    operator fun invoke(instrumentId: String, isVisible: Boolean): Completable =
        repository.setInstrumentVisibility(instrumentId, isVisible)
}