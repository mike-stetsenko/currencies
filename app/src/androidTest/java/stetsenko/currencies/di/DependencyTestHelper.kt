package stetsenko.currencies.di

import android.content.Context
import stetsenko.currencies.di.module.AppModule

fun buildTestComponent(context: Context): AppComponent {
    return DaggerTestAppComponent.builder()
            .appModule(AppModule(context))
            .build()
}
