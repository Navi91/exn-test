package ru.android.exn.test.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.android.exn.basic.dagger.ApplicationScope
import ru.android.exn.shared.quotes.data.datasource.InstrumentDao
import ru.android.exn.test.database.ExnDatabase

@Module
interface DatabaseModule {

    @Module
    companion object {

        @Provides
        @JvmStatic
        @ApplicationScope
        fun provideDataBase(context: Context): ExnDatabase =
            Room.databaseBuilder(context, ExnDatabase::class.java, "exn_db")
                .fallbackToDestructiveMigration()
                .build()

        @Provides
        @JvmStatic
        fun provideInstrumentPresentationDao(database: ExnDatabase): InstrumentDao =
            database.instrumentPresentationDao()
    }
}