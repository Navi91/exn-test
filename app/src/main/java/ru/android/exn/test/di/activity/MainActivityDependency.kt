package ru.android.exn.test.di.activity

import ru.android.exn.basic.navigation.NavEventProvider
import ru.android.exn.shared.quotes.data.datasource.QuotesSocket

interface MainActivityDependency {

    interface DependencyProvider {

        fun getMainActivityDependency(): MainActivityDependency
    }

    fun getNavigationEventProvider(): NavEventProvider

//    fun getQuotesSocket(): QuotesSocket
}