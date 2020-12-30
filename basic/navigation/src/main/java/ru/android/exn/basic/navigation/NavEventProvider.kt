package ru.android.exn.basic.navigation

import androidx.lifecycle.LiveData

interface NavEventProvider {

    fun postEvent(event: NavEvent)

    fun getEvents(): LiveData<NavEvent>
}