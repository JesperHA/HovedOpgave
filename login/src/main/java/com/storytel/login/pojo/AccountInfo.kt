package com.storytel.login.pojo

data class AccountInfo(val allLanguageObjects: List<Language>, val email: String, val lang: String,
                       val loginStatus: Int = 0, val phoneNumber: String? = null,
                       val refreshToken: String? = null, val singleSignToken: String? = null,
                       val userId: Int = 0, val connectedSocialId: String? = null,
                       val maximumNumberOfflineBooks: Int = 0, val paymentIssues: Boolean)