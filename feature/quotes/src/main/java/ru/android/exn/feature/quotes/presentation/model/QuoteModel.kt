package ru.android.exn.feature.quotes.presentation.model

sealed class QuoteModel(
    open val instrumentId: String,
    open val displayName: String
) {

    data class Empty(
        override val instrumentId: String,
        override val displayName: String
    ) : QuoteModel(instrumentId, displayName)

    data class Value(
        override val instrumentId: String,
        override val displayName: String,
        val bid: Float,
        val ask: Float,
        val spread: Float
    ) : QuoteModel(instrumentId, displayName)
}