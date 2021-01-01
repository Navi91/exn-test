package ru.android.exn.shared.quotes.di

import dagger.Binds
import dagger.Module
import ru.android.exn.shared.quotes.data.repository.QuotesSocketRepositoryImpl
import ru.android.exn.shared.quotes.domain.repository.QuotesSocketRepository

@Module
interface SharedQuotesModule {

    @Binds
    fun bindQuotesSocketRepository(repo: QuotesSocketRepositoryImpl): QuotesSocketRepository
}