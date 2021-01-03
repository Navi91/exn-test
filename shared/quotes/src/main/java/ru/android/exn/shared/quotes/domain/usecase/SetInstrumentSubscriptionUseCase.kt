package ru.android.exn.shared.quotes.domain.usecase

import io.reactivex.Completable
import ru.android.exn.shared.quotes.domain.repository.InstrumentRepository
import javax.inject.Inject

class SetInstrumentSubscriptionUseCase @Inject constructor(
    private val repository: InstrumentRepository
) {

    operator fun invoke(instrumentId: String, isSubscribed: Boolean): Completable =
        repository.setInstrumentVisibility(instrumentId, isSubscribed)
}