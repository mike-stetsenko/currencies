package stetsenko.currencies.view

import android.support.v7.app.AppCompatActivity
import stetsenko.currencies.CurrencyApp
import stetsenko.currencies.presenter.Presenter

abstract class PresenterActivity<P: Presenter<V>, V: View>: AppCompatActivity(), View {

    protected abstract val presenter: Presenter<V>
    protected abstract val view: V

    protected val appComponent get() = (application as CurrencyApp).appComponent

    override fun onStart() {
        super.onStart()

        presenter.onBind(view)
    }

    override fun onStop() {
        presenter.onUnbind(view)

        super.onStop()
    }
}