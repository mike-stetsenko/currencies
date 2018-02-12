package stetsenko.currencies.view

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import stetsenko.currencies.R
import stetsenko.currencies.presenter.Rate

class RatesAdapter : RecyclerView.Adapter<RatesAdapter.RatesViewHolder>() {

    interface Callback {
        fun onStartEdition(tag: String)
        fun onStopEditing(tag: String, value: String)
    }

    var clbk: Callback? = null

    private var rates: List<Rate> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_rate, parent, false)
        return RatesViewHolder(v)
    }

    fun setCallback(callback: Callback) {
        clbk = callback
    }

    override fun getItemCount(): Int = rates.count()

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        val rate = rates[position]
        with(holder) {
            abbr.text = rate.abbreviation
            descr.text = rate.description
            currencyValue.setText(rate.value)
            currencyIcon.setImageResource(rate.icon)

            currencyValue.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    currencyValue.clearFocus()
                    (abbr.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(currencyValue.windowToken, 0);
                }
                false
            }
            currencyValue.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                clbk?.let {
                    if (hasFocus) it.onStartEdition(rate.abbreviation)
                    else it.onStopEditing(rate.abbreviation, currencyValue.text.toString())
                }
            }
        }
    }

    class RatesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var abbr: TextView = itemView.findViewById(R.id.abbreviation)
        var descr: TextView = itemView.findViewById(R.id.description)
        var currencyValue: EditText = itemView.findViewById(R.id.currencyValue)
        var currencyIcon: ImageView = itemView.findViewById(R.id.currencyIcon)
    }

    fun update(rates: List<Rate>) {

        val diffResult = DiffUtil.calculateDiff(RateDiffCallback(this.rates, rates))

        this.rates = rates

        diffResult.dispatchUpdatesTo(this)

        notifyDataSetChanged()
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