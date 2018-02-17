package stetsenko.currencies.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import stetsenko.currencies.R
import stetsenko.currencies.presenter.CurrencyPresenter
import stetsenko.currencies.presenter.Rate
import javax.inject.Inject

class CurrenciesActivity : PresenterActivity<CurrencyPresenter, CurrenciesView>(), CurrenciesView,
    RatesAdapter.Callback {

    override val view: CurrenciesView get() = this

    @Inject
    override lateinit var presenter: CurrencyPresenter

    private lateinit var ratesRecycler: RecyclerView

    private val ratesAdapter = RatesAdapter().also { it.setCallback(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currencies)

        ratesRecycler = findViewById(R.id.rates)
        ratesRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        ratesRecycler.adapter = ratesAdapter
    }

    override fun updateRates(rates: List<Rate>) {
        ratesAdapter.update(rates)
    }

    override fun onStartEdition(tag: String, value: String) {
        ratesRecycler.scrollToPosition(0)
        presenter.onStartEdition(tag, value)
    }

    override fun onNewValue(tag: String, value: String) {
        presenter.onNewValue(tag, value)
    }

    override fun showError(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}
