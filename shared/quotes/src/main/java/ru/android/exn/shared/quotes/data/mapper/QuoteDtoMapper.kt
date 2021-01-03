package ru.android.exn.shared.quotes.data.mapper

import ru.android.exn.shared.quotes.data.dto.QuoteDto
import ru.android.exn.shared.quotes.domain.entity.Quote
import javax.inject.Inject

class QuoteDtoMapper @Inject constructor() {

    fun toQuote(quoteDto: QuoteDto): Quote =
        Quote(
            instrumentId = quoteDto.instrumentId,
            bid = quoteDto.bid,
            ask = quoteDto.ask,
            spread = quoteDto.spread
        )
}