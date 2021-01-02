package ru.android.exn.shared.quotes.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.android.exn.basic.dagger.ApplicationScope
import ru.android.exn.shared.quotes.data.datasource.QuotesSocket
import ru.android.exn.shared.quotes.data.repository.InstrumentRepositoryImpl
import ru.android.exn.shared.quotes.data.repository.QuotesRepositoryImpl
import ru.android.exn.shared.quotes.data.repository.QuotesSocketRepositoryImpl
import ru.android.exn.shared.quotes.domain.repository.InstrumentRepository
import ru.android.exn.shared.quotes.domain.repository.QuotesRepository
import ru.android.exn.shared.quotes.domain.repository.QuotesSocketRepository

@Module
interface SharedQuotesModule {

    @Module
    companion object {

        @Provides
        @ApplicationScope
        @JvmStatic
        fun provideQuotesSocket(): QuotesSocket =
            QuotesSocket()
    }

    @Binds
    fun bindQuotesSocketRepository(repo: QuotesSocketRepositoryImpl): QuotesSocketRepository

    @Binds
    fun bindQuotesRepository(repo: QuotesRepositoryImpl): QuotesRepository

    @Binds
    fun bindInstrumentRepository(repo: InstrumentRepositoryImpl): InstrumentRepository
}