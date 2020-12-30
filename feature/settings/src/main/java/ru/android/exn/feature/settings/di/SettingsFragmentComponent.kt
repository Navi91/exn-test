package ru.android.exn.feature.settings.di

import dagger.Component
import ru.android.exn.feature.settings.presentation.fragment.SettingsFragment

@Component(
    modules = [SettingsFragmentModule::class],
    dependencies = [SettingsFragmentDependency::class]
)
internal interface SettingsFragmentComponent {
    fun inject(settingsFragment: SettingsFragment)

    @Component.Factory
    interface Factory {

        fun create(dependency: SettingsFragmentDependency): SettingsFragmentComponent
    }
}