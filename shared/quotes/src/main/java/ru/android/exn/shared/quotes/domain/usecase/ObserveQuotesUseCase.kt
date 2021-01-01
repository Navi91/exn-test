package ru.android.exn.shared.quotes.domain.usecase

import io.reactivex.Observable
import ru.android.exn.shared.quotes.domain.entity.Quote
import ru.android.exn.shared.quotes.domain.repository.QuotesRepository
import javax.inject.Inject

class ObserveQuotesUseCase @Inject constructor(
        private val repository: QuotesRepository
) {

    operator fun invoke(): Observable<List<Quote>> =
            repository.observeQuotes()
}