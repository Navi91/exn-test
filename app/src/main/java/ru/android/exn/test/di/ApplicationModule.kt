package ru.android.exn.test.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.android.exn.test.ExnApplication
import ru.android.exn.test.database.DatabaseModule

@Module(
    includes = [DatabaseModule::class]
)
interface ApplicationModule {

    @Module
    companion object {
        @Provides
        @ApplicationScope
        fun provideContext(application: ExnApplication): Context =
            application
    }
}