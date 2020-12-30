package ru.android.exn.feature.settings.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import ru.android.exn.feature.settings.presentation.navigation.SettingsRouter
import javax.inject.Inject

internal class SettingsViewModel @Inject constructor(
    private val router: SettingsRouter
) : ViewModel() {

    fun back() {
        Log.d(LOG_TAG, "back")

        router.back()
    }

    private companion object {

        const val LOG_TAG = "SettingsViewModel"
    }
}