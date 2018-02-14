package stetsenko.currencies.presenter

import stetsenko.currencies.view.View

abstract class BasePresenter< V: View>: Presenter<V> {

    protected var view: V? = null

    abstract fun onBindView(view: V)

    override fun onBind(view: V) {
        this.view = view

        onBindView(view)
    }

    override fun onUnbind(view: V) {
        if(this.view === view){
            this.view = null
        }
    }

    override fun onFinish() {
        // release resources if needed
    }
}