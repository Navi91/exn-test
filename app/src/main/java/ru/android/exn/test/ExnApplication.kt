package ru.android.exn.test

import android.app.Application
import ru.android.exn.shared.quotes.data.datasource.QuotesSocket
import ru.android.exn.test.di.ApplicationComponent
import ru.android.exn.test.di.DaggerApplicationComponent
import ru.android.exn.test.di.activity.MainActivityDependency
import javax.inject.Inject

class ExnApplication : Application(), MainActivityDependency.DependencyProvider {

    private val component: ApplicationComponent by lazy {
        DaggerApplicationComponent
            .factory()
            .create(this)
    }

    @Inject
    lateinit var quotesSocket: QuotesSocket

    override fun onCreate() {
        super.onCreate()

        component.inject(this)

        quotesSocket.connect()
    }

    override fun getMainActivityDependency(): MainActivityDependency =
        component
}