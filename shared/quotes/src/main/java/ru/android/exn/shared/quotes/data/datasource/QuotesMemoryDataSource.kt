package ru.android.exn.shared.quotes.data.datasource

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import ru.android.exn.shared.quotes.data.dto.QuoteDto
import javax.inject.Inject

class QuotesMemoryDataSource @Inject constructor() {

    private val quotesSubject = BehaviorSubject.createDefault(emptyList<QuoteDto>())

    fun setQuotes(quotes: List<QuoteDto>) {
        quotesSubject.onNext(quotes)
    }

    fun observeQuotes(): Observable<List<QuoteDto>> =
        quotesSubject.hide()
}