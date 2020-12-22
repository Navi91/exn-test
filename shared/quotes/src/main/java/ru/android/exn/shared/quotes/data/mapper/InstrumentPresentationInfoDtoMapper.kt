package ru.android.exn.shared.quotes.data.mapper

import ru.android.exn.shared.quotes.data.dto.InstrumentPresentationInfoDto
import ru.android.exn.shared.quotes.domain.entity.InstrumentPresentationInfo
import javax.inject.Inject

class InstrumentPresentationInfoDtoMapper @Inject constructor() {

    fun toEntity(dto: InstrumentPresentationInfoDto): InstrumentPresentationInfo =
        InstrumentPresentationInfo(
            name = dto.id,
            isVisible = dto.isVisible,
            order = dto.order
        )
}