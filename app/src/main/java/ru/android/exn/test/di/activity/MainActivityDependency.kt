package ru.android.exn.test.di.activity

import ru.android.exn.basic.navigation.NavEventProvider
import ru.android.exn.shared.quotes.data.datasource.InstrumentDao
import ru.android.exn.shared.quotes.data.datasource.QuotesSocketDataSource
import ru.android.exn.shared.quotes.domain.repository.InstrumentRepository
import ru.android.exn.shared.quotes.domain.repository.QuotesRepository
import ru.android.exn.shared.quotes.domain.repository.QuotesSocketRepository

interface MainActivityDependency {

    interface DependencyProvider {

        fun getMainActivityDependency(): MainActivityDependency
    }

    fun getNavigationEventProvider(): NavEventProvider

    fun getQuotesSocketDataSource(): QuotesSocketDataSource

    fun getQuotesSocketRepository(): QuotesSocketRepository

    fun getQuotesRepository(): QuotesRepository

    fun getInstrumentDao(): InstrumentDao

    fun getInstrumentRepository(): InstrumentRepository
}