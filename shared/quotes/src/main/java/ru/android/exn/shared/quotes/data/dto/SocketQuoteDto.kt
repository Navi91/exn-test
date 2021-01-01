package ru.android.exn.shared.quotes.data.dto

import com.google.gson.annotations.SerializedName

data class SocketQuoteDto(
    @SerializedName("s") val id: String?,
    @SerializedName("b") val bid: String?,
    @SerializedName("a") val ask: String?,
    @SerializedName("spr") val spread: String?
)