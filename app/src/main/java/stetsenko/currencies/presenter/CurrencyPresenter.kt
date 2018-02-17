package stetsenko.currencies.presenter

import android.content.Context
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import stetsenko.currencies.R
import stetsenko.currencies.model.Rates
import stetsenko.currencies.model.RevolutApi
import stetsenko.currencies.view.CurrenciesView
import stetsenko.currencies.view.CurrencyPropertyProvider
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.TimeUnit

class CurrencyPresenter(private val context: Context,
                        private val api: RevolutApi,
                        private val currencyProps: CurrencyPropertyProvider) :
    BasePresenter<CurrenciesView>() {

    private var getRates = Disposables.disposed()

    private var currentCurrencyCode: String = "EUR"

    private var currentCurrencyValue: BigDecimal = BigDecimal.ONE

    private var ratesFromServer: List<Rate> = mutableListOf()

    private val calledRated: HashMap<String, Long> = hashMapOf()
    private var sortOrder = 0L

    private fun getRatesToShow(serverRates: List<Rate>,
                               rateCode: String,
                               rateValue: BigDecimal): List<Rate> {

        val initialValue = serverRates.first { it.code == rateCode }.value
        val multiplier = when (rateValue) {
            BigDecimal.ZERO -> BigDecimal.ZERO
            else -> rateValue.divide(initialValue, 2, RoundingMode.HALF_UP)
        }

        return serverRates
            .map {
                it.copy().apply {
                    value = when {
                        multiplier == BigDecimal.ZERO -> BigDecimal.ZERO
                        it.code == rateCode -> rateValue
                        else -> (value * multiplier)
                            .setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros()
                    }
                }
            }
            .sortedWith(ratesOrderComparator)
    }

    private val ratesOrderComparator = Comparator<Rate> { t1, t2 ->
        val lastPressed1 = calledRated[t1.code]
        val lastPressed2 = calledRated[t2.code]
        when {
            lastPressed1 == lastPressed2 -> 0
            lastPressed1 == null -> 1
            lastPressed2 == null -> -1
            lastPressed1 > lastPressed2 -> -1
            else -> 1
        }
    }

    override fun onBindView(view: CurrenciesView) {
        startUpdating()
    }

    private fun startUpdating() {
        if (!getRates.isDisposed) return

        getRates = api.getCurrencies()
            .subscribeOn(Schedulers.io())
            .repeatWhen { completed -> completed.delay(1, TimeUnit.SECONDS, Schedulers.io()) }
            .retryWhen { completed -> completed.delay(3, TimeUnit.SECONDS, Schedulers.io()) }
            .observeOn(Schedulers.computation())
            .map { res -> ratesToList(res.rates) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                ratesFromServer = res
                view?.updateRates(getRatesToShow(res, currentCurrencyCode, currentCurrencyValue))
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
        return Rates::class.java.declaredFields
            .filter { it.type == Float::class.java }
            .map { field ->
                field.isAccessible = true
                Pair(field.name, BigDecimal(field.getFloat(rates).toString()))
            }
            .toMutableList().apply { add(0, Pair("EUR", BigDecimal.ONE)) }
            .map { (name, value) ->
                Rate(
                    name, currencyProps.getDescription(name),
                    value, currencyProps.getIcon(name))
            }
    }

    fun onStartEdition(currencyCode: String, currencyValue: String) {
        calledRated[currencyCode] = sortOrder++
        currentCurrencyCode = currencyCode
        setCurrentCurrencyValue(currencyValue)
    }

    fun onNewValue(currencyCode: String, currencyValue: String) {
        if (currencyCode == currentCurrencyCode) {
            setCurrentCurrencyValue(currencyValue)
        }
    }

    private fun setCurrentCurrencyValue(currencyValue: String) {
        try {
            currentCurrencyValue = BigDecimal(currencyValue)
            view?.updateRates(
                getRatesToShow(ratesFromServer, currentCurrencyCode, currentCurrencyValue))
        } catch (e: NumberFormatException) {
            view?.showError(context.getString(R.string.wrong_input_error, currencyValue))
        }
    }
}