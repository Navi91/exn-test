package ru.android.exn.feature.quotes.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import ru.android.exn.feature.quotes.presentation.navigation.QuotesRouter
import javax.inject.Inject

internal class QuotesViewModel @Inject constructor(
    private val router: QuotesRouter
) : ViewModel() {

    fun openSettings() {
        Log.d(LOG_TAG, "openSettings")

        router.openSettings()
    }

    fun back() {
        Log.d(LOG_TAG, "back")

        router.back()
    }

    private companion object {

        const val LOG_TAG = "QuotesViewModel"
    }
}