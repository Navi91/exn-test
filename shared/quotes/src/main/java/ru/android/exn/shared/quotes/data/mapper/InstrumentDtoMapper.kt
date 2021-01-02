package ru.android.exn.shared.quotes.data.mapper

import ru.android.exn.shared.quotes.data.dto.InstrumentDto
import ru.android.exn.shared.quotes.domain.entity.Instrument
import javax.inject.Inject

class InstrumentDtoMapper @Inject constructor() {

    fun toInstrument(dto: InstrumentDto): Instrument =
        Instrument(
            id = dto.id,
            displayName = dto.displayName,
            isVisible = dto.isVisible,
            order = dto.order
        )
}