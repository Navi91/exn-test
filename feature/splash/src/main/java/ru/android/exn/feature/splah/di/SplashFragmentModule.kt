package ru.android.exn.feature.splah.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.android.exn.basic.dagger.FeatureScope
import ru.android.exn.basic.viewmodel.ViewModelFactory
import ru.android.exn.basic.viewmodel.ViewModelKey
import ru.android.exn.feature.splah.presentation.viewmodel.SplashViewModel

@Module
internal interface SplashFragmentModule {

    @Binds
    @FeatureScope
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel
}