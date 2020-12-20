package ru.android.exn.basic.navigation

import androidx.lifecycle.LiveData
import ru.android.exn.lib.singleliveevent.SingleLiveEvent
import javax.inject.Inject

class NavigationEventProviderImpl @Inject constructor() : NavigationEventProvider {

    private val eventBuffer = SingleLiveEvent<NavEvent>()

    override fun postEvent(event: NavEvent) {
        eventBuffer.postValue(event)
    }

    override fun getEvents(): LiveData<NavEvent> =
        eventBuffer
}