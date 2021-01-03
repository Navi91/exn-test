package ru.android.exn.shared.quotes.data.factory

import ru.android.exn.shared.quotes.data.dto.InstrumentDto
import javax.inject.Inject

class PrepopulateInstrumentFactory @Inject constructor() {

    fun createPrepopulateInstruments(): List<InstrumentDto> =
        listOf(
            InstrumentDto("BTCUSD", "BTC / USD", true),
            InstrumentDto("EURUSD", "EUR / USD", true),
            InstrumentDto("EURGBP", "EUR / GBP", true),
            InstrumentDto("USDJPY", "USD / JPY", true),
            InstrumentDto("USDCHF", "USD / CHF", true),
            InstrumentDto("USDCAD", "USD / CAD", true)
        )
}