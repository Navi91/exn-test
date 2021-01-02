package ru.android.exn.feature.settings.di

import ru.android.exn.basic.navigation.NavEventProvider
import ru.android.exn.shared.quotes.data.datasource.InstrumentDao
import ru.android.exn.shared.quotes.domain.repository.InstrumentRepository

interface SettingsFragmentDependency {

    interface DependencyProvider {

        fun getSettingsFragmentDependency(): SettingsFragmentDependency
    }

    fun getNavEventProvider(): NavEventProvider

    fun getInstrumentDao(): InstrumentDao

    fun getInstrumentRepository(): InstrumentRepository
}