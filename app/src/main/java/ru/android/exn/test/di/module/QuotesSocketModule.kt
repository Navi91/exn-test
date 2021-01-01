package ru.android.exn.test.di.module

import dagger.Module
import dagger.Provides
import ru.android.exn.basic.dagger.ApplicationScope
import ru.android.exn.shared.quotes.data.datasource.QuotesSocket

@Module
class QuotesSocketModule {

    @Module
    companion object {

        @Provides
        @ApplicationScope
        @JvmStatic
        fun provideQuotesSocket(): QuotesSocket =
            QuotesSocket()
    }
}