package stetsenko.currencies.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import stetsenko.currencies.di.scope.PerApplication

@Module
class AppModule(context: Context) {
    private val context: Context = context.applicationContext

    @Provides
    @PerApplication
    internal fun provideContext(): Context = context
}