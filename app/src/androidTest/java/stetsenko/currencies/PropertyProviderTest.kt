package stetsenko.currencies

import android.os.Build
import android.support.test.InstrumentationRegistry
import android.support.test.filters.MediumTest
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import stetsenko.currencies.view.CurrencyPropertyProvider
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@MediumTest
@RunWith(AndroidJUnit4::class)
class PropertyProviderTest {

    companion object {
        private const val CURRENCY_CODE_EXIST = "RUB"
        private const val CURRENCY_CODE_NOT_EXIST = "XXX"
    }

    private val appContext = InstrumentationRegistry.getTargetContext().applicationContext
    private lateinit var propertyProvider: CurrencyPropertyProvider

    @Before
    fun createPropertyProvider() {
        propertyProvider = CurrencyPropertyProvider(appContext)
    }

    @Test
    fun checkDescription() {

        val rub = propertyProvider.getDescription(CURRENCY_CODE_EXIST)
        Assert.assertNotEquals(rub, appContext.getString(R.string.unknown_currency))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Assert.assertEquals(rub, Currency.getInstance(CURRENCY_CODE_EXIST).displayName)
        } else {
            Assert.assertEquals(rub, appContext.getString(R.string.long_rub))
        }

        val unknownCurrency = propertyProvider.getDescription(CURRENCY_CODE_NOT_EXIST)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Assert.assertEquals(unknownCurrency, Currency.getInstance(CURRENCY_CODE_NOT_EXIST).displayName)
        } else {
            Assert.assertEquals(unknownCurrency, appContext.getString(R.string.unknown_currency))
        }
    }

    @Test
    fun checkIcon() {

        val rub = propertyProvider.getIcon(CURRENCY_CODE_EXIST)
        Assert.assertEquals(rub, R.drawable.flag_rub)

        val unknownCurrency = propertyProvider.getIcon(CURRENCY_CODE_NOT_EXIST)
        Assert.assertEquals(unknownCurrency, R.drawable.ic_launcher_background)
    }
}