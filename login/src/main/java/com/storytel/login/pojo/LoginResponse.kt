package com.storytel.login.pojo

data class LoginResponse(val accountInfo: AccountInfo?, val customerId: Int, val lang: String?,
                         val optionalBooks: Int, val premiumRealised: Boolean, val serverDateMillis: Long,
                         val result: String?)