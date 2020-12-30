package ru.android.exn.test.di.module

import dagger.Binds
import dagger.Module
import ru.android.exn.basic.dagger.ApplicationScope
import ru.android.exn.basic.navigation.NavEventProvider
import ru.android.exn.basic.navigation.NavEventProviderImpl

@Module
interface NavigationModule {

    @ApplicationScope
    @Binds
    fun bindNavigationEventProvider(provider: NavEventProviderImpl): NavEventProvider
}