package com.storytel.app.di

import com.storytel.app.FlowController
import com.storytel.login.feature.LoggedInNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module @InstallIn(ActivityComponent::class) abstract class NavigationFlowModule {
    @Binds abstract fun bindAnalyticsService(flowController: FlowController): LoggedInNavigator
}