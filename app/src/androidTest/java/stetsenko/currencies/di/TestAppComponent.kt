package stetsenko.currencies.di

import dagger.Component
import stetsenko.currencies.di.module.ApiModule
import stetsenko.currencies.di.module.AppModule
import stetsenko.currencies.di.module.PresenterModule
import stetsenko.currencies.di.module.ViewPropsModule
import stetsenko.currencies.di.scope.PerApplication
import stetsenko.currencies.view.CurrenciesActivity

@PerApplication
@Component(
    // TODO Provide at least stub for ApiModule in order to check UI in details apart of network state
    modules = [(ViewPropsModule::class), (AppModule::class), (ApiModule::class), (PresenterModule::class)])
interface TestAppComponent : AppComponent {
    override fun inject(activity: CurrenciesActivity)
}