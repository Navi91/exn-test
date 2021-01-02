package ru.android.exn.shared.quotes.data.mapper

import ru.android.exn.shared.quotes.data.dto.InstrumentDto
import ru.android.exn.shared.quotes.domain.entity.InstrumentPresentationInfo
import javax.inject.Inject

class InstrumentPresentationInfoDtoMapper @Inject constructor() {

    fun toEntity(dto: InstrumentDto): InstrumentPresentationInfo =
        InstrumentPresentationInfo(
            name = dto.id,
            isVisible = dto.isVisible,
            order = dto.order
        )
}