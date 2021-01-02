package ru.android.exn.feature.settings.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.recyclerview.widget.RecyclerView
import ru.android.exn.feature.settings.R
import ru.android.exn.feature.settings.databinding.LayoutInstrumentItemBinding
import ru.android.exn.feature.settings.presentation.adapter.InstrumentsAdapter.InstrumentItemViewHolder
import ru.android.exn.feature.settings.presentation.model.InstrumentModel

internal class InstrumentsAdapter : RecyclerView.Adapter<InstrumentItemViewHolder>() {

    private val items = mutableListOf<InstrumentModel>()

    fun setItems(items: List<InstrumentModel>) {
        this.items.apply {
            clear()
            addAll(items)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstrumentItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_instrument_item,
            parent,
            false
        )

        return InstrumentItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: InstrumentItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int =
        items.size

    class InstrumentItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = LayoutInstrumentItemBinding.bind(view)
        private val instrumentCheckedTextView: CheckedTextView
            get() = binding.instrumentCheckedTextView

        fun bind(item: InstrumentModel) {
            instrumentCheckedTextView.isChecked = item.isChecked
            instrumentCheckedTextView.text = item.displayName
        }
    }
}