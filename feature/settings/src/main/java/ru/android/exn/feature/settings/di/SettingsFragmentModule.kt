package ru.android.exn.feature.settings.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.android.exn.basic.viewmodel.ViewModelFactory
import ru.android.exn.basic.viewmodel.ViewModelKey
import ru.android.exn.feature.settings.presentation.viewmodel.SettingsViewModel

@Module
internal interface SettingsFragmentModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel
}