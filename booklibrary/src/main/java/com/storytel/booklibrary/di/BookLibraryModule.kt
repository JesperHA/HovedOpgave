package com.storytel.booklibrary.di

import com.storytel.booklibrary.api.BookLibraryApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object BookLibraryModule {
    @Singleton @Provides fun provideLoginWebService(retrofit: Retrofit): BookLibraryApi {
        return retrofit.create(BookLibraryApi::class.java)
    }
}