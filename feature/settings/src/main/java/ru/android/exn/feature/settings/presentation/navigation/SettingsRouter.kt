package ru.android.exn.feature.settings.presentation.navigation

import ru.android.exn.basic.navigation.NavEventProvider
import ru.android.exn.basic.navigation.UpNavEvent
import javax.inject.Inject

internal class SettingsRouter @Inject constructor(
    private val navEventProvider: NavEventProvider
) {

    fun back() {
        navEventProvider.postEvent(UpNavEvent)
    }
}