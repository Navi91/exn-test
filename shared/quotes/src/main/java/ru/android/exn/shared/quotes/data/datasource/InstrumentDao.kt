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

    @Transaction
    private fun getAllInternal(): List<InstrumentDto> {
        val instruments = getAll()

        return if (instruments.isEmpty()) {
            insertAll(
                listOf(
                    InstrumentDto("BTCUSD", "BTC / USD", true, 0),
                    InstrumentDto("EURUSD", "EUR / USD", true, 1),
                    InstrumentDto("EURGBP", "EUR / GBP", true, 2),
                    InstrumentDto("USDJPY", "USD / JPY", true, 3),
                    InstrumentDto("USDCHF", "USD / CHF", true, 4),
                    InstrumentDto("USDCAD", "USD / CAD", true, 5)
                )
            )

            getAll()
        } else {
            instruments
        }
    }

    @Insert
    fun insertAll(dtoList: List<InstrumentDto>)

    @Query("UPDATE InstrumentDto SET isVisible = :isVisible WHERE id = :instrumentId")
    fun update(instrumentId: String, isVisible: Boolean): Completable
}