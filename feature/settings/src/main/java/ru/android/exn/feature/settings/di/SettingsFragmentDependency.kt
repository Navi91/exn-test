package ru.android.exn.feature.settings.di

import ru.android.exn.basic.navigation.NavEventProvider

interface SettingsFragmentDependency {

    interface DependencyProvider {

        fun getSettingsFragmentDependency(): SettingsFragmentDependency
    }

    fun getNavEventProvider(): NavEventProvider
}