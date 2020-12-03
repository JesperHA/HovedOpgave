package com.storytel.login.feature.login

import com.storytel.login.pojo.LoginValidation
import java.util.Collections

data class UserLoginUiModel(val isLoading: Boolean = false, val errorMessage: String = "",
                            val isLoggedIn: Boolean = false,
                            val inputErrorValidation: List<LoginValidation> = Collections.emptyList()) {
}