package com.storytel.app.network

import com.storytel.login.data.UserCredentialStore
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(private val userCredentialStore: UserCredentialStore):
        Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val loginResponse = userCredentialStore.get()
        val authToken = loginResponse.accountInfo?.singleSignToken?.ifBlank { "" }
        val userId = loginResponse.accountInfo?.userId ?: 0
        val request = chain.request()
        val urlBuilder = request.url.newBuilder()
        urlBuilder.addQueryParameter("token", authToken).addQueryParameter("userid", userId.toString())
                .addQueryParameter("terminal", "android")
        val requestWithToken = request.newBuilder().url(urlBuilder.build()).build()
//        Timber.i("Token: " + authToken)
        return chain.proceed(requestWithToken)

    }
}