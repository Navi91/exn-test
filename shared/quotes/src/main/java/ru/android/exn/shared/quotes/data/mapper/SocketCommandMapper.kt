package ru.android.exn.shared.quotes.data.mapper

import javax.inject.Inject

class SocketCommandMapper @Inject constructor() {

    fun toSubscribeCommand(instrumentId: String): String =
        "SUBSCRIBE: $instrumentId"

    fun toUnsubscribeCommand(instrumentId: String): String =
        "UNSUBSCRIBE: $instrumentId"
}