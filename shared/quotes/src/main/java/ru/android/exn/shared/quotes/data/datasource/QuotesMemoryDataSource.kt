package ru.android.exn.shared.quotes.data.datasource

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import ru.android.exn.shared.quotes.data.dto.QuoteDto
import javax.inject.Inject

class QuotesMemoryDataSource @Inject constructor() {

    private val quotesSubject = BehaviorSubject.createDefault(emptyList<QuoteDto>())

    fun updateQuotes(updateQuotes: List<QuoteDto>) : Completable  = quotesSubject
        .firstElement()
        .map { it.toMutableList() }
        .doOnSuccess { quotes ->

            updateQuotes.forEach { updateQuoteDto ->
                val existQuoteDto = quotes.firstOrNull { quoteDto ->
                    quoteDto.instrumentId == updateQuoteDto.instrumentId
                }

                if (existQuoteDto != null) {
                    quotes.remove(existQuoteDto)
                }

                quotes.add(updateQuoteDto)
            }
        }
        .doOnSuccess { quotes -> quotesSubject.onNext(quotes) }
        .ignoreElement()

    fun observeQuotes(): Observable<List<QuoteDto>> =
        quotesSubject.hide()
}