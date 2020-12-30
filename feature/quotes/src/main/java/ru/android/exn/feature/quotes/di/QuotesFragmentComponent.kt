package ru.android.exn.feature.quotes.di

import dagger.BindsInstance
import dagger.Component
import ru.android.exn.basic.dagger.FeatureScope
import ru.android.exn.feature.quotes.presentation.fragment.QuotesFragment

@Component(
    dependencies = [QuotesFragmentDependency::class],
    modules = [QuotesFragmentModule::class]
)
@FeatureScope
interface QuotesFragmentComponent {
    fun inject(quotesFragment: QuotesFragment)

    @Component.Factory
    interface Factory {

        fun create(dependency: QuotesFragmentDependency): QuotesFragmentComponent
    }
}