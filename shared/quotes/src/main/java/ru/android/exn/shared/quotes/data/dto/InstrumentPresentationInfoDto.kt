package ru.android.exn.shared.quotes.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "InstrumentPresentationInfoDto"
)
data class InstrumentPresentationInfoDto(
    @PrimaryKey val id: String,
    val isVisible: Boolean,
    val order: Int
)