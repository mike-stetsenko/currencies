package stetsenko.currencies.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import stetsenko.currencies.di.scope.PerApplication
import stetsenko.currencies.model.RevolutApi
import stetsenko.currencies.presenter.CurrencyPresenter

@Module
class PresenterModule {
    @Provides
    @PerApplication
    internal fun provideCurrencyPresenter(context: Context, api: RevolutApi): CurrencyPresenter =
        CurrencyPresenter(context, api)
}