package ru.android.exn.feature.quotes.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.android.exn.feature.quotes.presentation.model.QuoteModel

class QuotesDiffUtilCallback(
    private val oldItems: List<QuoteModel>,
    private val newItems: List<QuoteModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int =
        oldItems.size

    override fun getNewListSize(): Int =
        newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItems[oldItemPosition].instrumentId == newItems[newItemPosition].instrumentId

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItems[oldItemPosition] == newItems[newItemPosition]
}