package ru.android.exn.shared.quotes.data.dto

data class QuoteDto(
    val id: String,
    val bid: Float,
    val ask: Float,
    val spread: Float
)