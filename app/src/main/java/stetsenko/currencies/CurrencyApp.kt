package stetsenko.currencies

import android.app.Application
import stetsenko.currencies.di.AppComponent
import stetsenko.currencies.di.DaggerAppComponent
import stetsenko.currencies.di.module.AppModule

class CurrencyApp : Application() {
    internal lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = buildComponent()
    }

    private fun buildComponent(): AppComponent {
        return DaggerAppComponent.builder()
            .appModule(AppModule(applicationContext))
            .build()
    }
}