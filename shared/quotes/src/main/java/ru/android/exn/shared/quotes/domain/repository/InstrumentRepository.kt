package ru.android.exn.shared.quotes.domain.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.android.exn.shared.quotes.domain.entity.Instrument

interface InstrumentRepository {

    fun getInstruments(): Single<List<Instrument>>

    fun observeInstruments(): Observable<List<Instrument>>

    fun setInstrumentVisibility(instrumentId: String, isVisible: Boolean) : Completable

    fun prepopulateIfNeed() :Completable
}