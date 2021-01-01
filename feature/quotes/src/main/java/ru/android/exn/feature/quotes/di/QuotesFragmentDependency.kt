package ru.android.exn.feature.quotes.di

import ru.android.exn.basic.navigation.NavEventProvider
import ru.android.exn.shared.quotes.domain.repository.QuotesRepository
import ru.android.exn.shared.quotes.domain.repository.QuotesSocketRepository

interface QuotesFragmentDependency {

    interface DependencyProvider {

        fun getQuotesFragmentDependency(): QuotesFragmentDependency
    }

    fun getNavigationEventProvider(): NavEventProvider

    fun getQuotesSocketRepository() : QuotesSocketRepository

    fun getQuotesRepository(): QuotesRepository
}