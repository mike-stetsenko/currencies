package stetsenko.currencies.di.module

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import stetsenko.currencies.di.scope.PerApplication
import stetsenko.currencies.model.RevolutApi

@Module
class ApiModule {
    @PerApplication
    @Provides
    fun provideApi(): RevolutApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://revolut.duckdns.org/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create<RevolutApi>(RevolutApi::class.java)
    }
}