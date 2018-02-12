package stetsenko.currencies.presenter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import stetsenko.currencies.R
import stetsenko.currencies.model.Rates
import stetsenko.currencies.model.RevolutApi
import stetsenko.currencies.view.CurrenciesView
import java.util.*
import java.util.concurrent.TimeUnit

class CurrencyPresenter(private val context: Context, private val api: RevolutApi) :
    BasePresenter<CurrenciesView>() {

    private var getRates = Disposables.disposed()

    override fun onBindView(view: CurrenciesView) {
        startUpdating()
    }

    private fun startUpdating(){
        getRates = api.getCurrencies()
            .subscribeOn(Schedulers.io())
            .repeatWhen { completed -> completed.delay(1, TimeUnit.SECONDS) }
            .retryWhen { completed -> completed.delay(3, TimeUnit.SECONDS) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                (view as CurrenciesView).updateRates(ratesToList(res.rates))
            }, { err ->
                Toast.makeText(context, err.message, Toast.LENGTH_LONG).show()
            })
    }

    override fun onUnbind(view: CurrenciesView) {
        stopUpdating()
        super.onUnbind(view)
    }

    private fun stopUpdating() {
        if (!getRates.isDisposed) {
            getRates.dispose()
        }
    }

    private fun ratesToList(rates: Rates): List<Rate> {
        return Rates::class.java.declaredFields.map { field ->
            field.isAccessible = true
            Pair(field.name, field.getFloat(rates).toString())
        }
            .toMutableList().apply { add(0, Pair("EUR", "1")) }
            .map { (name, value) -> Rate(name, getDescription(name), value, getIcon(name)) }
    }

    fun onStartEdition(tag: String) {
        stopUpdating()
    }

    fun onStopEdition(tag: String, value: String) {
        startUpdating()
    }

    @SuppressLint("NewApi")
    private fun getDescription(currencyCode: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Currency.getInstance(currencyCode).displayName
            } catch (e: Exception){
                context.getString(R.string.unknown_currency)
            }

        } else {
            val id = when (currencyCode) {
                "AUD" -> R.string.long_aud
                "EUR" -> R.string.long_eur
                "USD" -> R.string.long_usd
                "JPY" -> R.string.long_jpy
                "BGN" -> R.string.long_bgn
                "CZK" -> R.string.long_czk
                "DKK" -> R.string.long_dkk
                "GBP" -> R.string.long_gbp
                "HUF" -> R.string.long_huf
                "PLN" -> R.string.long_pln
                "RON" -> R.string.long_ron
                "SEK" -> R.string.long_sek
                "CHF" -> R.string.long_chf
                "NOK" -> R.string.long_nok
                "HRK" -> R.string.long_hrk
                "RUB" -> R.string.long_rub
                "TRY" -> R.string.long_try
                "BRL" -> R.string.long_brl
                "CAD" -> R.string.long_cad
                "CNY" -> R.string.long_cny
                "HKD" -> R.string.long_hkd
                "IDR" -> R.string.long_idr
                "ILS" -> R.string.long_ils
                "INR" -> R.string.long_inr
                "KRW" -> R.string.long_krw
                "MXN" -> R.string.long_mxn
                "MYR" -> R.string.long_myr
                "NZD" -> R.string.long_nzd
                "PHP" -> R.string.long_php
                "SGD" -> R.string.long_sgd
                "THB" -> R.string.long_thb
                "ZAR" -> R.string.long_zar
                else -> R.string.unknown_currency
            }
            context.getString(id)
        }
    }

    private fun getIcon(currencyCode: String): Int {
        return when (currencyCode) {
            "AUD" -> R.drawable.flag_aud
            "EUR" -> R.drawable.flag_eur
            "USD" -> R.drawable.flag_usd
            "JPY" -> R.drawable.flag_jpy
            "BGN" -> R.drawable.flag_bgn
            "CZK" -> R.drawable.flag_czk
            "DKK" -> R.drawable.flag_dkk
            "GBP" -> R.drawable.flag_gbp
            "HUF" -> R.drawable.flag_huf
            "PLN" -> R.drawable.flag_pln
            "RON" -> R.drawable.flag_ron
            "SEK" -> R.drawable.flag_sek
            "CHF" -> R.drawable.flag_chf
            "NOK" -> R.drawable.flag_nok
            "HRK" -> R.drawable.flag_hrk
            "RUB" -> R.drawable.flag_rub
            "TRY" -> R.drawable.flag_try
            "BRL" -> R.drawable.flag_brl
            "CAD" -> R.drawable.flag_cad
            "CNY" -> R.drawable.flag_cny
            "HKD" -> R.drawable.flag_hkd
            "IDR" -> R.drawable.flag_idr
            "ILS" -> R.drawable.flag_ils
            "INR" -> R.drawable.flag_inr
            "KRW" -> R.drawable.flag_kpw
            "MXN" -> R.drawable.flag_mxn
            "MYR" -> R.drawable.flag_myr
            "NZD" -> R.drawable.flag_nzd
            "PHP" -> R.drawable.flag_php
            "SGD" -> R.drawable.flag_sgd
            "THB" -> R.drawable.flag_thb
            "ZAR" -> R.drawable.flag_zar
            else -> R.drawable.ic_launcher_background
        }
    }
}