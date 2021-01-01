package ru.android.exn.test.di.activity

import ru.android.exn.basic.navigation.NavEventProvider
import ru.android.exn.shared.quotes.data.datasource.QuotesSocket
import ru.android.exn.shared.quotes.domain.repository.QuotesRepository
import ru.android.exn.shared.quotes.domain.repository.QuotesSocketRepository

interface MainActivityDependency {

    interface DependencyProvider {

        fun getMainActivityDependency(): MainActivityDependency
    }

    fun getNavigationEventProvider(): NavEventProvider

    fun getQuotesSocket(): QuotesSocket

    fun getQuotesSocketRepository(): QuotesSocketRepository

    fun getQuotesRepository(): QuotesRepository
}