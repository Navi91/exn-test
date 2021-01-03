package ru.android.exn.shared.quotes.data.datasource

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.android.exn.shared.quotes.data.dto.InstrumentDto

@Dao
interface InstrumentDao {

    @Query("SELECT * FROM InstrumentDto")
    fun getAllSingle(): Single<List<InstrumentDto>>

    @Insert
    fun insertAll(dtoList: List<InstrumentDto>): Completable

    @Query("UPDATE InstrumentDto SET isSubscribed = :isVisible WHERE id = :instrumentId")
    fun update(instrumentId: String, isVisible: Boolean): Completable

}