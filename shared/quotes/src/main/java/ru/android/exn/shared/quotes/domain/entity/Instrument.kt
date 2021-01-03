package ru.android.exn.shared.quotes.domain.entity

data class Instrument(
    val id: String,
    val displayName: String,
    val isSubscribed: Boolean
)