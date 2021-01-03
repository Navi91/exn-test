package ru.android.exn.shared.quotes.domain.usecase

import io.reactivex.Completable
import ru.android.exn.shared.quotes.domain.repository.InstrumentRepository
import ru.android.exn.shared.quotes.domain.repository.QuotesSocketRepository
import javax.inject.Inject

class SetInstrumentSubscriptionUseCase @Inject constructor(
    private val repository: InstrumentRepository,
    private val socketRepository: QuotesSocketRepository
) {

    operator fun invoke(instrumentId: String, isSubscribed: Boolean): Completable =
        repository.setInstrumentVisibility(instrumentId, isSubscribed)
            .andThen(
                if (isSubscribed) {
                    socketRepository.subscribe(instrumentId)
                } else {
                    socketRepository.unsubscribe(instrumentId)
                }
            )

}