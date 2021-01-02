package ru.android.exn.test.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.android.exn.shared.quotes.data.datasource.InstrumentDao
import ru.android.exn.shared.quotes.data.dto.InstrumentDto

@Database(
    entities = [InstrumentDto::class],
    version = 1
)
abstract class ExnDatabase : RoomDatabase() {

    abstract fun instrumentPresentationDao(): InstrumentDao
}