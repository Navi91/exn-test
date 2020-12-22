package ru.android.exn.shared.quotes.data.datasource

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Completable
import io.reactivex.Single
import ru.android.exn.shared.quotes.data.dto.InstrumentPresentationInfoDto

@Dao
interface InstrumentPresentationDao {

    @Query("SELECT * FROM InstrumentPresentationInfoDto")
    fun getAll(): Single<List<InstrumentPresentationInfoDto>>

    @Update
    fun update(infoDto: InstrumentPresentationInfoDto): Completable
}