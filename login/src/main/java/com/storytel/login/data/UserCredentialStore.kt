package com.storytel.login.data

import androidx.annotation.WorkerThread
import com.storytel.login.pojo.LoginResponse

interface UserCredentialStore {
    @WorkerThread
    fun save(loginResponse: LoginResponse)
    fun get(): LoginResponse
}