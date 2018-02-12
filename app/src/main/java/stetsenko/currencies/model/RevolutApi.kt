package stetsenko.currencies.model

import io.reactivex.Single
import retrofit2.http.GET

interface RevolutApi {
    @GET("latest?base=EUR")
    fun getCurrencies(): Single<CurrenciesResponse>
}

class CurrenciesResponse(val base: String, val date: String, val rates: Rates)

class Rates(var AUD: Float,
            var BGN: Float,
            var BRL: Float,
            var CAD: Float,
            var CHF: Float,
            var CNY: Float,
            var CZK: Float,
            var DKK: Float,
            var GBP: Float,
            var HKD: Float,
            var HRK: Float,
            var HUF: Float,
            var IDR: Float,
            var ILS: Float,
            var INR: Float,
            var ISK: Float,
            var JPY: Float,
            var KRW: Float,
            var MXN: Float,
            var MYR: Float,
            var NOK: Float,
            var NZD: Float,
            var PHP: Float,
            var PLN: Float,
            var RON: Float,
            var RUB: Float,
            var SEK: Float,
            var SGD: Float,
            var THB: Float,
            var TRY: Float,
            var USD: Float,
            var ZAR: Float)
