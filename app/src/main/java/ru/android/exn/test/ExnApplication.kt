package ru.android.exn.test

import android.app.Application
import ru.android.exn.test.di.ApplicationComponent
import ru.android.exn.test.di.DaggerApplicationComponent

class ExnApplication : Application() {

    private val component: ApplicationComponent by lazy {
        DaggerApplicationComponent
            .factory()
            .create(this)
    }

    override fun onCreate() {
        super.onCreate()
    }
}