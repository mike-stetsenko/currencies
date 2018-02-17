package stetsenko.currencies.helpers

import android.support.test.espresso.IdlingResource
import android.support.test.espresso.idling.CountingIdlingResource


class RxEspressoScheduleHandler : io.reactivex.functions.Function<Runnable, Runnable> {

    private val countingIdlingResource = CountingIdlingResource("rxJava")

    @Throws(Exception::class)
    override fun apply(runnable: Runnable): Runnable {
        return Runnable {
            countingIdlingResource.increment()

            try {
                runnable.run()
            } finally {
                countingIdlingResource.decrement()
            }
        }
    }

    val idlingResource: IdlingResource
        get() = countingIdlingResource

}