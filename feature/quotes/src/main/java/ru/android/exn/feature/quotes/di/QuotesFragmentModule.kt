package ru.android.exn.feature.quotes.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.android.exn.basic.viewmodel.ViewModelFactory
import ru.android.exn.basic.viewmodel.ViewModelKey
import ru.android.exn.feature.quotes.presentation.viewmodel.QuotesViewModel

@Module
interface QuotesFragmentModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(QuotesViewModel::class)
    fun bindQuotesViewModel(viewModel: QuotesViewModel): ViewModel
}