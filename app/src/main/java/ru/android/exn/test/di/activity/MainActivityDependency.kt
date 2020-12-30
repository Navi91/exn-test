package ru.android.exn.test.di.activity

import ru.android.exn.basic.navigation.NavEventProvider

interface MainActivityDependency {

    interface DependencyProvider {

        fun getMainActivityDependency(): MainActivityDependency
    }

    fun getNavigationEventProvider(): NavEventProvider
}