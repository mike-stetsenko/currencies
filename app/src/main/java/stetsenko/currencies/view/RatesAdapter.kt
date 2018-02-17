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

    companion object {
        private const val POSTFIX = "..."
        private const val ZERO_STRING = "0"
    }

    interface Callback {
        fun onStartEdition(tag: String, value: String)
        fun onNewValue(tag: String, value: String)
    }

    private var clbk: Callback? = null

    private var rates: List<Rate> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_rate, parent, false)
        return RatesViewHolder(v)
    }

    fun setCallback(callback: Callback) {
        clbk = callback
    }

    override fun getItemCount(): Int = rates.count()

    private fun getOnChangeWatcher(block: () -> Unit): TextWatcher = object : TextWatcher {

        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
            block()
        }
    }

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        val rate = rates[position]
        with(holder) {
            abbr.text = rate.code
            descr.text = rate.description
            currencyIcon.setImageResource(rate.icon)

            val sum = rate.value.toPlainString()
            // --> during currency conversion, one value can by much longer than another
            //     lets show them as ellipsized by "end" manually - this property
            //     not compatible with inputType <--
            val maxLength = holder.currencyValue.resources.getInteger(R.integer.input_max_length)
            val sumToShow =
                if (sum.length > maxLength)
                    sum.substring(0, maxLength - POSTFIX.length - 1) + POSTFIX
                else sum
            currencyValue.setText(sumToShow)

            val textChangedWatcher = getOnChangeWatcher {
                if (holder.currencyValue.hasFocus()) {
                    val newVal = currencyValue.text.toString()
                    clbk?.onNewValue(rate.code, if (newVal.isBlank()) ZERO_STRING else newVal)
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
                        // --> manually rough removing ellipsizing from values <--
                        val value = currencyValue.text.toString().replace(POSTFIX, "")
                        it.onStartEdition(rate.code, value)
                        currencyValue.addTextChangedListener(textChangedWatcher)
                    } else {
                        currencyValue.removeTextChangedListener(textChangedWatcher)
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

        if (this.rates.isNotEmpty() && rates.isNotEmpty() && this.rates[0].code == rates[0].code) {
            // no need to redraw first row with base currency
            this.rates[0].value = rates[0].value
        }

        val diffResult = DiffUtil.calculateDiff(RateDiffCallback(this.rates, rates))

        this.rates = rates

        try {
            // TODO
            // --> somehow, call notifyItemRangeChanged with DiffUtil leads to
            //     incorrect ImageView redraw but notifyDataSetChanged() doesn't <--
            diffResult.dispatchUpdatesTo(this)
        } catch (e: IllegalStateException) {
            // never mind, I'll redraw this on next step
        }
    }

    private class RateDiffCallback(val old: List<Rate>, val new: List<Rate>) :
        DiffUtil.Callback() {

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