package stetsenko.currencies

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import io.reactivex.plugins.RxJavaPlugins
import org.hamcrest.Matchers
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import stetsenko.currencies.di.buildTestComponent
import stetsenko.currencies.helpers.RecyclerViewItemCountAssertion
import stetsenko.currencies.helpers.RxEspressoScheduleHandler
import stetsenko.currencies.view.CurrenciesActivity

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class CurrencyActivityTest {

    @Rule
    @JvmField
    val activityTestRule = IntentsTestRule(CurrenciesActivity::class.java)

    companion object {
        @BeforeClass
        @JvmStatic
        fun tearUp() {
            val appContext = InstrumentationRegistry.getTargetContext().applicationContext
            (appContext as CurrencyApp).appComponent = buildTestComponent(appContext)

            val rxEspressoScheduleHandler = RxEspressoScheduleHandler()
            RxJavaPlugins.setScheduleHandler(rxEspressoScheduleHandler)
            IdlingRegistry.getInstance().register(rxEspressoScheduleHandler.idlingResource)
        }
    }

    @Test
    fun showRates() {
        Espresso.onView(ViewMatchers.withId(R.id.rates))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check(RecyclerViewItemCountAssertion.withItemCount(Matchers.greaterThan(30)))
            .check(RecyclerViewItemCountAssertion.withItemCount(Matchers.lessThan(40)))
    }
}
