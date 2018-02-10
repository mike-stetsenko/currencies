package stetsenko.currencies.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import stetsenko.currencies.R
import stetsenko.currencies.presenter.CurrencyPresenter
import stetsenko.currencies.presenter.Rate
import javax.inject.Inject

class CurrenciesActivity : PresenterActivity<CurrencyPresenter, CurrenciesView>(), CurrenciesView {

    override val view: CurrenciesView get() = this

    @Inject
    override lateinit var presenter: CurrencyPresenter

    lateinit var rates: RecyclerView

    private val ratesAdapter = RatesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currencies)

        rates = findViewById(R.id.rates)
        rates.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rates.adapter = ratesAdapter
    }

    override fun updateRates(rates: List<Rate>) {
        ratesAdapter.update(rates)
    }
}
