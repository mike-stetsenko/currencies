package stetsenko.currencies.presenter

import stetsenko.currencies.view.View

interface Presenter<in V: View> {
    fun onBind(view: V)

    fun onUnbind(view: V)

    fun onFinish()
}