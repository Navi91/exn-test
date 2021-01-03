package ru.android.exn.feature.splah.di

import ru.android.exn.basic.navigation.NavEventProvider
import ru.android.exn.shared.quotes.domain.repository.InstrumentRepository

interface SplashFragmentDependency {

    interface DependencyProvider {

        fun getSplashFragmentDependency(): SplashFragmentDependency
    }

    fun getNavEventProvider(): NavEventProvider

    fun getInstrumentRepository(): InstrumentRepository
}