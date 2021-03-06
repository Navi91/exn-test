package ru.android.exn.shared.quotes.data.mapper

import android.util.Log
import com.google.gson.Gson
import ru.android.exn.shared.quotes.data.dto.QuoteDto
import ru.android.exn.shared.quotes.data.dto.SocketMessageDto
import ru.android.exn.shared.quotes.data.dto.SocketQuoteDto
import javax.inject.Inject

class SocketMessageMapper @Inject constructor(
    private val gson: Gson
) {

    fun toQuoteDtoList(message: String): List<QuoteDto> {
        val socketMessageDto = gson.fromJson(message, SocketMessageDto::class.java)

        if (socketMessageDto == null) {
            Log.w(LOG_TAG, "Data is not present in message: $message")

            return emptyList()
        }

        val quoteDtoList = socketMessageDto.ticks

        if (quoteDtoList.isNullOrEmpty()) {
            Log.w(LOG_TAG, "Ticks is not present in message: $message")

            return emptyList()
        }

        return quoteDtoList
            .reversed()
            .distinctBy { quoteDto -> quoteDto.id }
            .mapNotNull { quoteDto -> toQuoteDto(quoteDto) }
    }

    private fun toQuoteDto(dto: SocketQuoteDto): QuoteDto? {
        val instrumentId = dto.id

        if (instrumentId == null) {
            Log.w(LOG_TAG, "Display name is not present in $dto")

            return null
        }

        val bid = dto.bid?.toFloatOrNull()

        if (bid == null) {
            Log.w(LOG_TAG, "Bid is not present in $dto")

            return null
        }

        val ask = dto.ask?.toFloatOrNull()

        if (ask == null) {
            Log.w(LOG_TAG, "Ask is not present in $dto")

            return null
        }

        val spread = dto.spread?.toFloatOrNull()

        if (spread == null) {
            Log.w(LOG_TAG, "Spread in not present in $dto")

            return null
        }

        return QuoteDto(instrumentId, bid, ask, spread)
    }

    private companion object {

        const val LOG_TAG = "SocketMessageMapper"
    }
}