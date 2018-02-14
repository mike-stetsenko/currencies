package stetsenko.currencies.view

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
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
        fun onNewValue(tag: String, value: String)
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
            abbr.text = rate.code
            descr.text = rate.description
            currencyValue.setText(rate.value.toPlainString())
            currencyIcon.setImageResource(rate.icon)

            val textChangedWatcher = object : TextWatcher {

                override fun afterTextChanged(p0: Editable?) {
                    // no need
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // no need
                }

                override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
                    val newVal = currencyValue.text.toString()
                    clbk?.onNewValue(rate.code, if (newVal.isBlank()) "0" else newVal)
                }
            }

            currencyValue.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    currencyValue.clearFocus()
                    (abbr.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(currencyValue.windowToken, 0)
                }
                false
            }
            currencyValue.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                clbk?.let {
                    if (hasFocus) {
                        it.onStartEdition(rate.code)
                        currencyValue.addTextChangedListener(textChangedWatcher)
                    } else {
                        currencyValue.removeTextChangedListener(textChangedWatcher)
                        it.onStopEditing(rate.code, currencyValue.text.toString())
                    }
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

        if (this.rates.isNotEmpty() && rates.isNotEmpty() && this.rates[0].code == rates[0].code){
            // prevent first row redraw in order not to lose edittext focus
            // TODO get rid of this
            this.rates[0].value = rates[0].value
        }

        val diffResult = DiffUtil.calculateDiff(RateDiffCallback(this.rates, rates))

        this.rates = rates

        diffResult.dispatchUpdatesTo(this)
    }

    private class RateDiffCallback(val old: List<Rate>, val new: List<Rate>) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return old[oldItemPosition].code == new[newItemPosition].code
        }

        override fun getOldListSize(): Int = old.size

        override fun getNewListSize(): Int = new.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return old[oldItemPosition] == new[newItemPosition]
        }
    }
}