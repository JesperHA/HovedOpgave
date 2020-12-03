package com.storytel.login.pojo

data class UserLogin(val uId: LoginValidation, val password: LoginValidation)

data class LoginValidation(val input : LoginInput, val isValid: Boolean = true, val message: String = "")