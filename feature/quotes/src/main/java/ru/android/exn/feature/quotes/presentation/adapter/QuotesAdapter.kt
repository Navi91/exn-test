package ru.android.exn.feature.quotes.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.android.exn.feature.quotes.R
import ru.android.exn.feature.quotes.databinding.LayoutQuoteItemBinding
import ru.android.exn.feature.quotes.presentation.adapter.QuotesAdapter.QuotesItemViewHolder
import ru.android.exn.feature.quotes.presentation.model.QuoteModel

class QuotesAdapter : RecyclerView.Adapter<QuotesItemViewHolder>() {

    val items = mutableListOf<QuoteModel>()

    fun setItems(items: List<QuoteModel>) {
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
        private val bindTextView: TextView
            get() = binding.bidTextView
        private val askTextView: TextView
            get() = binding.askTextView
        private val spreadTextView: TextView
            get() = binding.spreadTextView

        fun bind(item: QuoteModel) {
            instrumentTextView.text = item.displayName

            when (item) {
                is QuoteModel.Empty -> {
                    bindTextView.text =
                        itemView.context.getString(R.string.undefined_value)
                    askTextView.text =
                        itemView.context.getString(R.string.undefined_value)
                    spreadTextView.text = itemView.context.getString(R.string.undefined_value)
                }
                is QuoteModel.Value -> {
                    bindTextView.text = "${item.bid}"
                    askTextView.text = "${item.ask}"
                    spreadTextView.text = item.spread.toString()
                }
            }

        }
    }
}