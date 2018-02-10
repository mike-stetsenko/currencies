package stetsenko.currencies.model

import io.reactivex.Single
import retrofit2.http.GET

interface RevolutApi {
    @GET("latest?base=EUR")
    fun getCurrencies(): Single<CurrenciesResponse>
}

class CurrenciesResponse(val base: String, val date: String, val rates: Rates)

class Rates(val AUD: Float,
            val BGN: Float,
            val BRL: Float,
            val CAD: Float,
            val CHF: Float,
            val CNY: Float,
            val CZK: Float,
            val DKK: Float,
            val GBP: Float,
            val HKD: Float,
            val HRK: Float,
            val HUF: Float,
            val IDR: Float,
            val ILS: Float,
            val INR: Float,
            val ISK: Float,
            val JPY: Float,
            val KRW: Float,
            val MXN: Float,
            val MYR: Float,
            val NOK: Float,
            val NZD: Float,
            val PHP: Float,
            val PLN: Float,
            val RON: Float,
            val RUB: Float,
            val SEK: Float,
            val SGD: Float,
            val THB: Float,
            val TRY: Float,
            val USD: Float,
            val ZAR: Float)
