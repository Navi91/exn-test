package ru.android.exn.shared.quotes.data.datasource

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ru.android.exn.shared.quotes.data.dto.InstrumentDto

@Dao
interface InstrumentDao {

    fun getAllSingle(): Single<List<InstrumentDto>> = Single
        .fromCallable { getAllInternal() }

    @Query("SELECT * FROM InstrumentDto")
    fun getAll(): List<InstrumentDto>

    @Insert
    fun insertAll(dtoList: List<InstrumentDto>)

    @Query("UPDATE InstrumentDto SET isSubscribed = :isVisible WHERE id = :instrumentId")
    fun update(instrumentId: String, isVisible: Boolean): Completable


    @Transaction
    private fun getAllInternal(): List<InstrumentDto> {
        val instruments = getAll()

        return if (instruments.isEmpty()) {
            insertAll(
                listOf(
                    InstrumentDto("BTCUSD", "BTC / USD", true),
                    InstrumentDto("EURUSD", "EUR / USD", true),
                    InstrumentDto("EURGBP", "EUR / GBP", true),
                    InstrumentDto("USDJPY", "USD / JPY", true),
                    InstrumentDto("USDCHF", "USD / CHF", true),
                    InstrumentDto("USDCAD", "USD / CAD", true)
                )
            )

            getAll()
        } else {
            instruments
        }
    }
}