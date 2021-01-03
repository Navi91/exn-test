package ru.android.exn.test.di.activity

import dagger.Component
import ru.android.exn.feature.quotes.di.QuotesFragmentDependency
import ru.android.exn.feature.settings.di.SettingsFragmentDependency
import ru.android.exn.feature.splah.di.SplashFragmentDependency
import ru.android.exn.test.presentation.activity.MainActivity

@Component(
    dependencies = [MainActivityDependency::class]
)
interface MainActivityComponent :
    QuotesFragmentDependency,
    SettingsFragmentDependency,
    SplashFragmentDependency {

    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {

        fun create(dependency: MainActivityDependency): MainActivityComponent
    }
}