package ru.android.exn.test.di

import dagger.BindsInstance
import dagger.Component
import ru.android.exn.basic.dagger.ApplicationScope
import ru.android.exn.feature.quotes.di.QuotesFragmentDependency
import ru.android.exn.test.ExnApplication

@Component(
    modules = [ApplicationModule::class]
)
@ApplicationScope
interface ApplicationComponent : QuotesFragmentDependency {

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance app: ExnApplication): ApplicationComponent
    }
}