package ru.android.exn.feature.splah.presentation.navigation

import ru.android.exn.basic.navigation.NavEventProvider
import javax.inject.Inject

internal class SplashRouter @Inject constructor(
    private val navEventProvider: NavEventProvider
) {

    fun openQuotesScreen() {
        navEventProvider.postEvent(OpenQuotesScreen)
    }
}