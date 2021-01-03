package ru.android.exn.shared.quotes.data.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.android.exn.shared.quotes.data.dto.InstrumentDto

@Dao
interface InstrumentDao {

    @Query("SELECT * FROM InstrumentDto")
    fun getAll(): Single<List<InstrumentDto>>

    @Query("SELECT * FROM InstrumentDto")
    fun observeAll(): Observable<List<InstrumentDto>>

    @Insert
    fun insertAll(dtoList: List<InstrumentDto>): Completable

    @Query("UPDATE InstrumentDto SET isSubscribed = :isVisible WHERE id = :instrumentId")
    fun update(instrumentId: String, isVisible: Boolean): Completable

}