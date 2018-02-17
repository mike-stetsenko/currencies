package stetsenko.currencies.di

import dagger.Component
import stetsenko.currencies.di.module.ApiModule
import stetsenko.currencies.di.module.AppModule
import stetsenko.currencies.di.module.PresenterModule
import stetsenko.currencies.di.module.ViewPropsModule
import stetsenko.currencies.di.scope.PerApplication
import stetsenko.currencies.view.CurrenciesActivity

@PerApplication
@Component(modules = [(AppModule::class), (PresenterModule::class), (ApiModule::class), (ViewPropsModule::class)])
interface AppComponent {
    fun inject(activity: CurrenciesActivity)

}