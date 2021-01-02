package ru.android.exn.feature.settings.presentation.model

internal data class InstrumentModel(
    val id: String,
    val displayName: String,
    val order: Int,
    val isChecked: Boolean
)