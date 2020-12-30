package ru.android.exn.feature.quotes.presentation.navigation

import ru.android.exn.basic.navigation.ExitNavEvent
import ru.android.exn.basic.navigation.NavEventProvider
import javax.inject.Inject

class QuotesRouter @Inject constructor(
    private val navEventProvider: NavEventProvider
) {

    fun back() {
        navEventProvider.postEvent(ExitNavEvent)
    }

    fun openSettings() {

    }
}