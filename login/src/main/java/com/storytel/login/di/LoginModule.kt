package com.storytel.login.di

import android.content.Context
import com.google.gson.Gson
import com.storytel.login.R
import com.storytel.login.api.LoginWebService
import com.storytel.login.data.SharedPrefsUserCredentialStore
import com.storytel.login.data.UserCredentialStore
import com.storytel.login.encryption.AppCrypto
import com.storytel.login.encryption.PasswordCrypto
import com.storytel.login.feature.create.credentials.country.CountryUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import java.util.Locale
import javax.inject.Named
import javax.inject.Singleton

@Module @InstallIn(ApplicationComponent::class) object LoginModule {

    @Provides fun providePasswordCrypto(): PasswordCrypto {
        return AppCrypto()
    }

    @Provides fun provideWelcomeDescriptions(@ApplicationContext context: Context): Array<String> {
        return arrayOf(context.getString(R.string.welcome_screen_description_1),
                context.getString(R.string.welcome_screen_description_2),
                context.getString(R.string.welcome_screen_description_3))
    }

    @Provides @Singleton fun provideUserCredentialStore(@ApplicationContext context: Context,
                                                        gson: Gson): UserCredentialStore {
        return SharedPrefsUserCredentialStore(context, gson)
    }

    @Singleton @Provides fun provideLoginWebService(retrofit: Retrofit): LoginWebService {
        return retrofit.create(LoginWebService::class.java)
    }

    @Provides @Named("DeviceLocale") fun provideDeviceLocale(): String {
        return Locale.getDefault().toString()
    }

    @Provides fun provideCountryUtil(): CountryUtil {
        return CountryUtil()
    }
}