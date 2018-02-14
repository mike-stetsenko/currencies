package stetsenko.currencies.presenter

import java.math.BigDecimal

data class Rate(val code: String,
                val description: String,
                var value: BigDecimal,
                val icon: Int)