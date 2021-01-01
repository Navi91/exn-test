package ru.android.exn.shared.quotes.domain.repository

import io.reactivex.Observable
import ru.android.exn.shared.quotes.domain.entity.Quote

interface QuotesRepository {

    fun observeQuotes(): Observable<List<Quote>>
}