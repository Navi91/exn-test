package ru.android.exn.test.di

import dagger.BindsInstance
import dagger.Component
import ru.android.exn.basic.dagger.ApplicationScope
import ru.android.exn.feature.quotes.di.QuotesFragmentDependency
import ru.android.exn.test.ExnApplication
import ru.android.exn.test.di.activity.MainActivityDependency
import ru.android.exn.test.di.module.ApplicationModule

@Component(
    modules = [ApplicationModule::class]
)
@ApplicationScope
interface ApplicationComponent : QuotesFragmentDependency, MainActivityDependency {

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance app: ExnApplication): ApplicationComponent
    }
}