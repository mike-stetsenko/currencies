package stetsenko.currencies.view

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import stetsenko.currencies.R
import stetsenko.currencies.presenter.Rate

class RatesAdapter : RecyclerView.Adapter<RatesAdapter.RatesViewHolder>() {

    private var rates: List<Rate> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_rate, parent, false)
        return RatesViewHolder(v)
    }

    override fun getItemCount(): Int = rates.count()

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        val rate = rates[position]
        with(holder) {
            abbr.text = rate.abbreviation
            descr.text = rate.description
            currencyValue.text = rate.value
        }
    }

    class RatesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var abbr: TextView = itemView.findViewById(R.id.abbreviation)
        var descr: TextView = itemView.findViewById(R.id.description)
        var currencyValue: TextView = itemView.findViewById(R.id.currencyValue)
    }

    fun update(rates: List<Rate>) {

        val diffResult = DiffUtil.calculateDiff(RateDiffCallback(this.rates, rates))

        this.rates = rates

        diffResult.dispatchUpdatesTo(this)
    }

    private class RateDiffCallback(val old: List<Rate>, val new: List<Rate>) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return old[oldItemPosition].abbreviation == new[newItemPosition].abbreviation
        }

        override fun getOldListSize(): Int = old.size

        override fun getNewListSize(): Int = new.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return old[oldItemPosition] == new[newItemPosition]
        }
    }
}