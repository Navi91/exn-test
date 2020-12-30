package ru.android.exn.feature.quotes.di

import ru.android.exn.basic.navigation.NavEventProvider

interface QuotesFragmentDependency {

    interface DependencyProvider {

        fun getQuotesFragmentDependency(): QuotesFragmentDependency
    }

    fun getNavigationEventProvider(): NavEventProvider
}