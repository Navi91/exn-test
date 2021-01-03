package ru.android.exn.feature.quotes.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.android.exn.feature.quotes.R
import ru.android.exn.feature.quotes.databinding.LayoutQuoteItemBinding
import ru.android.exn.feature.quotes.presentation.adapter.QuotesAdapter.QuotesItemViewHolder
import ru.android.exn.shared.quotes.domain.entity.Quote

class QuotesAdapter : RecyclerView.Adapter<QuotesItemViewHolder>() {

    private val items = mutableListOf<Quote>()

    fun setItems(items: List<Quote>) {
        this.items.apply {
            clear()
            addAll(items)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotesItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_quote_item,
            parent,
            false
        )

        return QuotesItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuotesItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int =
        items.size

    class QuotesItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = LayoutQuoteItemBinding.bind(view)
        private val instrumentTextView: TextView
            get() = binding.instrumentTextView
        private val bindAndAskTextView: TextView
            get() = binding.bidAndAskTextView
        private val spreadTextView: TextView
            get() = binding.spreadTextView

        fun bind(item: Quote) {
            instrumentTextView.text = item.id
            bindAndAskTextView.text = "${item.bid} / ${item.ask}"
            spreadTextView.text = item.spread.toString()
        }
    }
}