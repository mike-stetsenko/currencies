package stetsenko.currencies.presenter

import android.content.Context
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import stetsenko.currencies.model.Rates
import stetsenko.currencies.model.RevolutApi
import stetsenko.currencies.view.CurrenciesView
import java.util.concurrent.TimeUnit

class CurrencyPresenter(private val context: Context, private val api: RevolutApi) :
    BasePresenter<CurrenciesView>() {

    private var getRates = Disposables.disposed()

    override fun onBindView(view: CurrenciesView) {
        getRates = api.getCurrencies()
            .subscribeOn(Schedulers.io())
            .repeatWhen { completed -> completed.delay(1, TimeUnit.SECONDS) }
            .retryWhen { completed -> completed.delay(3, TimeUnit.SECONDS) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                view.updateRates(ratesToList(res.rates))
            }, { err ->
                Toast.makeText(context, err.message, Toast.LENGTH_LONG).show()
            })
    }

    private fun ratesToList(rates: Rates): List<Rate> {
        return listOf(
            Rate("AUD", "AUD", rates.AUD.toString(), 0),
            Rate("BGN", "BGN", rates.BGN.toString(), 0),
            Rate("BRL", "BRL", rates.BRL.toString(), 0),
            Rate("CAD", "CAD", rates.CAD.toString(), 0))
    }
}