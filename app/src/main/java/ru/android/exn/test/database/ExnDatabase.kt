package ru.android.exn.test.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.android.exn.shared.quotes.data.datasource.InstrumentPresentationDao
import ru.android.exn.shared.quotes.data.dto.InstrumentPresentationInfoDto

@Database(
    entities = [InstrumentPresentationInfoDto::class],
    version = 1
)
abstract class ExnDatabase : RoomDatabase() {

    abstract fun instrumentPresentationDao(): InstrumentPresentationDao
}