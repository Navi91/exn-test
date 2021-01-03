package ru.android.exn.shared.quotes.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.android.exn.basic.dagger.ApplicationScope
import ru.android.exn.shared.quotes.data.QuotesWebSocketFactory
import ru.android.exn.shared.quotes.data.datasource.QuotesSocketDataSource
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

        @JvmStatic
        @Provides
        @ApplicationScope
        fun provideQuotesSocketDataSource(factory: QuotesWebSocketFactory): QuotesSocketDataSource =
            QuotesSocketDataSource(factory)
    }

    @Binds
    fun bindQuotesSocketRepository(repo: QuotesSocketRepositoryImpl): QuotesSocketRepository

    @Binds
    fun bindQuotesRepository(repo: QuotesRepositoryImpl): QuotesRepository

    @Binds
    fun bindInstrumentRepository(repo: InstrumentRepositoryImpl): InstrumentRepository
}