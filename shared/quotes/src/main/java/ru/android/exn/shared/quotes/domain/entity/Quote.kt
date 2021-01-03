package ru.android.exn.shared.quotes.domain.entity

data class Quote(
        val instrumentId: String,
        val bid: Float,
        val ask: Float,
        val spread: Float
)