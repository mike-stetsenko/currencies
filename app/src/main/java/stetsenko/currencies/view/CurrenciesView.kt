package stetsenko.currencies.view

import stetsenko.currencies.presenter.Rate

interface CurrenciesView: View {
    fun updateRates(rates: List<Rate>)
}