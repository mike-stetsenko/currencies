package stetsenko.currencies.di.module

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import stetsenko.currencies.di.scope.PerApplication
import stetsenko.currencies.model.RevolutApi

@Module
class ApiModule {

    companion object {
        private const val BASE_URL = "https://revolut.duckdns.org/"
    }

    @PerApplication
    @Provides
    fun provideApi(): RevolutApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)).build())
            .build()

        return retrofit.create<RevolutApi>(RevolutApi::class.java)
    }
}