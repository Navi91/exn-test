package ru.android.exn.feature.settings.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.android.exn.feature.settings.presentation.model.InstrumentModel

internal class InstrumentDiffUtilsCallback(
    private val oldItems: List<InstrumentModel>,
    private val newItems: List<InstrumentModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int =
        oldItems.size

    override fun getNewListSize(): Int =
        newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItems[oldItemPosition].id == newItems[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItems[oldItemPosition] == newItems[newItemPosition]

}