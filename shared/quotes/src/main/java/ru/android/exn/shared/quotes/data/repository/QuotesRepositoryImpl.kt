package ru.android.exn.shared.quotes.data.repository

import android.util.Log
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.android.exn.shared.quotes.data.datasource.QuotesMemoryDataSource
import ru.android.exn.shared.quotes.data.datasource.QuotesSocketDataSource
import ru.android.exn.shared.quotes.data.mapper.QuoteDtoMapper
import ru.android.exn.shared.quotes.data.mapper.SocketMessageMapper
import ru.android.exn.shared.quotes.domain.entity.Quote
import ru.android.exn.shared.quotes.domain.repository.QuotesRepository
import javax.inject.Inject

class QuotesRepositoryImpl @Inject constructor(
    quotesSocketDataSource: QuotesSocketDataSource,
    private val quotesMemoryDataSource: QuotesMemoryDataSource,
    private val messageMapper: SocketMessageMapper,
    private val quoteDtoMapper: QuoteDtoMapper
) : QuotesRepository {

    init {
        quotesSocketDataSource.observeMessage()
            .observeOn(Schedulers.io())
            .map { message -> messageMapper.toQuoteDtoList(message) }
            .doOnNext { quoteDtoList -> Log.v(LOG_TAG, "Update quotes: $quoteDtoList") }
            .concatMapCompletable { quoteDtoList ->
                quotesMemoryDataSource.updateQuotes(quoteDtoList)
            }
            .subscribeBy(
                onComplete = {
                    Log.d(LOG_TAG, "Observe message completed")
                },
                onError = { error ->
                    Log.e(LOG_TAG, "Observe message error: $error")
                }
            )
    }

    override fun observeQuotes(): Observable<List<Quote>> = quotesMemoryDataSource
        .observeQuotes()
        .map { quoteDtoList ->
            quoteDtoList.map { quoteDto -> quoteDtoMapper.toQuote(quoteDto) }
        }
        .subscribeOn(Schedulers.io())

    private companion object {

        const val LOG_TAG = "QuotesRepositoryImpl"
    }
}