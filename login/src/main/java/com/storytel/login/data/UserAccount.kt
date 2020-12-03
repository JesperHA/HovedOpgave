package com.storytel.login.data

import javax.inject.Inject

class UserAccount @Inject constructor(private val userCredentialStore: UserCredentialStore) {
    fun isSignedIn(): Boolean {
        val loginResponse = userCredentialStore.get()
        return loginResponse.accountInfo?.singleSignToken?.isNotBlank() == true
    }
}