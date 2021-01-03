package ru.android.exn.test.presentation.navigation

import ru.android.exn.basic.navigation.NavEventProvider
import javax.inject.Inject

class MainActivityRouter @Inject constructor(
    private val navEventProvider: NavEventProvider
) {

    fun openStartScreen() {
        navEventProvider.postEvent(OpenStartScreen)
    }
}