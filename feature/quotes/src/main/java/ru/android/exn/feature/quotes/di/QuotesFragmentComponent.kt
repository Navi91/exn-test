package ru.android.exn.feature.quotes.di

import dagger.BindsInstance
import dagger.Component
import ru.android.exn.basic.dagger.FeatureScope

@Component(
    dependencies = [QuotesFragmentDependency::class],
    modules = [QuotesFragmentModule::class]
)
@FeatureScope
interface QuotesFragmentComponent {

    @Component.Factory
    interface Factory {

        fun create(dependency: QuotesFragmentDependency): QuotesFragmentComponent
    }
}