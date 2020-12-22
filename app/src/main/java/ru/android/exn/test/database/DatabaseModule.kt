package ru.android.exn.test.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.android.exn.shared.quotes.data.datasource.InstrumentPresentationDao
import ru.android.exn.test.di.ApplicationScope

@Module
interface DatabaseModule {

    @Module
    companion object {

        @Provides
        @ApplicationScope
        fun provideDataBase(context: Context): ExnDatabase =
            Room.databaseBuilder(context, ExnDatabase::class.java, "exn_db")
                .fallbackToDestructiveMigration()
                .build()

        @Provides
        fun provideInstrumentPresentationDao(database: ExnDatabase): InstrumentPresentationDao =
            database.instrumentPresentationDao()
    }
}