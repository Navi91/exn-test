package ru.android.exn.shared.quotes.data.repository

import io.reactivex.Observable
import ru.android.exn.shared.quotes.data.datasource.QuotesSocket
import ru.android.exn.shared.quotes.data.mapper.SocketMessageMapper
import ru.android.exn.shared.quotes.domain.entity.Quote
import ru.android.exn.shared.quotes.domain.repository.QuotesRepository
import javax.inject.Inject

class QuotesRepositoryImpl @Inject constructor(
        private val socket: QuotesSocket,
        private val messageMapper: SocketMessageMapper
) : QuotesRepository {

    override fun observeQuotes(): Observable<List<Quote>> = socket
            .observeMessages()
            .map { message -> messageMapper.toQuotes(message) }
}