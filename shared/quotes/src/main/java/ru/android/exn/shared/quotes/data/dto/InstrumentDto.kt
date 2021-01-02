package ru.android.exn.shared.quotes.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "InstrumentDto"
)
data class InstrumentDto(
    @PrimaryKey val id: String,
    val displayName: String,
    val isVisible: Boolean,
    val order: Int
)