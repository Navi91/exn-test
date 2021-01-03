package ru.android.exn.test.di.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.android.exn.basic.viewmodel.ViewModelFactory
import ru.android.exn.basic.viewmodel.ViewModelKey
import ru.android.exn.test.presentation.viewmodel.MainActivityViewModel

@Module
interface MainActivityModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    fun bindMainActivityViewModel(viewModel: MainActivityViewModel): ViewModel
}