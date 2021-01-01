package ru.android.exn.test.di

import dagger.BindsInstance
import dagger.Component
import ru.android.exn.basic.dagger.ApplicationScope
import ru.android.exn.shared.quotes.di.SharedQuotesModule
import ru.android.exn.test.ExnApplication
import ru.android.exn.test.di.activity.MainActivityDependency
import ru.android.exn.test.di.module.ApplicationModule
import ru.android.exn.test.di.module.QuotesSocketModule

@Component(
    modules = [
        ApplicationModule::class,
        QuotesSocketModule::class,
        SharedQuotesModule::class
    ]
)
@ApplicationScope
interface ApplicationComponent : MainActivityDependency {
    fun inject(exnApplication: ExnApplication)

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance app: ExnApplication): ApplicationComponent
    }
}