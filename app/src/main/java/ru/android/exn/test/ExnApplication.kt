package ru.android.exn.test

import android.app.Application
import ru.android.exn.test.di.ApplicationComponent
import ru.android.exn.test.di.DaggerApplicationComponent
import ru.android.exn.test.di.activity.MainActivityDependency

class ExnApplication : Application(), MainActivityDependency.DependencyProvider {

    private val component: ApplicationComponent by lazy {
        DaggerApplicationComponent
            .factory()
            .create(this)
    }

    override fun onCreate() {
        super.onCreate()

        component.inject(this)
    }

    override fun getMainActivityDependency(): MainActivityDependency =
        component
}