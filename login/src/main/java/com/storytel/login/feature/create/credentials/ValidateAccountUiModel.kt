package com.storytel.login.feature.create.credentials

import com.storytel.login.pojo.LoginValidation
import java.util.Collections

data class ValidateAccountUiModel(val isLoading: Boolean = false, val errorMessage: String = "",
                             val isValid: Boolean = false,
                             val inputErrorValidation: List<LoginValidation> = Collections.emptyList()) {}