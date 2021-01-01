package ru.android.exn.test.di.module

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import ru.android.exn.basic.dagger.ApplicationScope
import ru.android.exn.test.ExnApplication

@Module(
        includes = [DatabaseModule::class, NavigationModule::class]
)
interface ApplicationModule {

    @Module
    companion object {

        @Provides
        @JvmStatic
        @ApplicationScope
        fun provideContext(application: ExnApplication): Context =
                application

        @Provides
        @JvmStatic
        @ApplicationScope
        fun provideGson(): Gson =
            Gson()
    }
}