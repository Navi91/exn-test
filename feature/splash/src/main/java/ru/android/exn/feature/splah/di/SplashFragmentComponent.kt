package ru.android.exn.feature.splah.di

import dagger.Component
import ru.android.exn.basic.dagger.FeatureScope
import ru.android.exn.feature.splah.presentation.fragment.SplashFragment

@FeatureScope
@Component(
    dependencies = [SplashFragmentDependency::class],
    modules = [SplashFragmentModule::class]
)
interface SplashFragmentComponent {
    fun inject(splashFragment: SplashFragment)

    @Component.Factory
    interface Factory {

        fun create(dependency: SplashFragmentDependency) : SplashFragmentComponent
    }
}