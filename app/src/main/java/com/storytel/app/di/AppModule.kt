package com.storytel.app.di

import android.content.Context
import com.storytel.booklibrary.data.SLBookDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @Provides @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = SLBookDatabase.provideDb(context)

    @Provides @Singleton
    fun provideDao(@ApplicationContext context: Context) = SLBookDatabase.provideDb(context).slBookRoomDatabaseDao


}
