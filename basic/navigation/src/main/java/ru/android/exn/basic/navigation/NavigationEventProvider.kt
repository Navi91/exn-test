package ru.android.exn.basic.navigation

import androidx.lifecycle.LiveData

interface NavigationEventProvider {

    fun postEvent(event: NavEvent)

    fun getEvents(): LiveData<NavEvent>
}