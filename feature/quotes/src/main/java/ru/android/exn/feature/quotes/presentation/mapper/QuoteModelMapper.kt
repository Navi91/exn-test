package ru.android.exn.feature.quotes.presentation.mapper

import ru.android.exn.feature.quotes.presentation.model.QuoteModel
import ru.android.exn.shared.quotes.domain.entity.Instrument
import ru.android.exn.shared.quotes.domain.entity.Quote
import javax.inject.Inject

class QuoteModelMapper @Inject constructor() {

    fun toQuoteModel(instrument: Instrument, quote: Quote?): QuoteModel =
        if (quote == null) {
            QuoteModel.Empty(instrument.id, instrument.displayName)
        } else {
            QuoteModel.Value(
                instrument.id,
                instrument.displayName,
                quote.bid,
                quote.ask,
                quote.spread
            )
        }
}