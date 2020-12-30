package ru.android.exn.test.di.activity

import dagger.Component
import ru.android.exn.feature.quotes.di.QuotesFragmentDependency
import ru.android.exn.test.MainActivity

@Component(
    dependencies = [MainActivityDependency::class]
)
interface MainActivityComponent : QuotesFragmentDependency {
    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {

        fun create(dependency: MainActivityDependency): MainActivityComponent
    }
}