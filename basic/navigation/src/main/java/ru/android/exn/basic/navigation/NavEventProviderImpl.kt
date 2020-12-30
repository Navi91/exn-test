package ru.android.exn.basic.navigation

import android.util.Log
import androidx.lifecycle.LiveData
import ru.android.exn.lib.singleliveevent.SingleLiveEvent
import javax.inject.Inject

class NavEventProviderImpl @Inject constructor() : NavEventProvider {

    private val eventBuffer = SingleLiveEvent<NavEvent>()

    override fun postEvent(event: NavEvent) {
        Log.d(LOG_TAG, "postEvent event: $event")

        eventBuffer.postValue(event)
    }

    override fun getEvents(): LiveData<NavEvent> =
        eventBuffer

    private companion object {

        const val LOG_TAG = "NavigationEventProviderImpl"
    }
}