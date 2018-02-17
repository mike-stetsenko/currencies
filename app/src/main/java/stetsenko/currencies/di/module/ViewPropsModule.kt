package stetsenko.currencies.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import stetsenko.currencies.di.scope.PerApplication
import stetsenko.currencies.view.CurrencyPropertyProvider

@Module
class ViewPropsModule {

    @PerApplication
    @Provides
    fun provideCurrencyProperty(context: Context): CurrencyPropertyProvider =
        CurrencyPropertyProvider(context)
}