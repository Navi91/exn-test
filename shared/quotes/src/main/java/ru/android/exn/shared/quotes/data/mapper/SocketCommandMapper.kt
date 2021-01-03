package ru.android.exn.shared.quotes.data.mapper

import ru.android.exn.shared.quotes.domain.entity.Instrument
import javax.inject.Inject

class SocketCommandMapper @Inject constructor() {

    fun toSubscribeCommand(instrument: Instrument): String =
        "SUBSCRIBE: ${instrument.id}"

    fun toUnsubscribeCommand(instrument: Instrument): String =
        "UNSUBSCRIBE: ${instrument.id}"
}