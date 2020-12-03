package com.storytel.app.di

import android.content.Context
import com.google.gson.Gson
import com.storytel.app.BuildConfig
import com.storytel.app.network.TokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module @InstallIn(ApplicationComponent::class) object NetworkModule {
    private val SIZE_OF_CACHE = 1024.toLong() * 1024 * 10

    @Provides @Singleton fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL_API)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(okHttpClient).build()
    }

    @Provides @Singleton fun provideHttpCache(context: Context): Cache {
        return Cache(context.getDir("httpCache", Context.MODE_PRIVATE), SIZE_OF_CACHE)
    }

    @Provides @Singleton fun provideOkHttpClient(tokenInterceptor: TokenInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(15, TimeUnit.SECONDS)
        builder.readTimeout(15, TimeUnit.SECONDS)
        builder.writeTimeout(15, TimeUnit.SECONDS)
        builder.retryOnConnectionFailure(false)
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.HEADERS
            builder.addInterceptor(interceptor)
        }
        builder.addInterceptor(tokenInterceptor)

        return builder.build()
    }

    @Provides @Singleton fun providesGson(): Gson {
        return Gson()
    }
}