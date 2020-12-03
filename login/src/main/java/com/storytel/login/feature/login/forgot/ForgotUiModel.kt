package com.storytel.login.feature.login.forgot

import com.storytel.login.pojo.LoginValidation
import java.util.Collections

data class ForgotUiModel(val isLoading: Boolean = false, val errorMessage: String = "",
                         val hasResetPassword: Boolean = false,
                         val inputErrorValidation: List<LoginValidation> = Collections.emptyList())