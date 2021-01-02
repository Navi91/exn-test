package ru.android.exn.feature.settings.presentation.mapper

import ru.android.exn.feature.settings.presentation.model.InstrumentModel
import ru.android.exn.shared.quotes.domain.entity.Instrument
import javax.inject.Inject

internal class InstrumentModelMapper @Inject constructor() {

    fun toInstrumentModel(instrument: Instrument): InstrumentModel =
        InstrumentModel(
            id = instrument.id,
            displayName = instrument.displayName,
            order = instrument.order,
            isChecked = instrument.isVisible
        )
}