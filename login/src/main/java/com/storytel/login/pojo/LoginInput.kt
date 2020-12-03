package com.storytel.login.pojo

import com.storytel.login.R
import com.storytel.base.util.ResourceProvider

data class LoginInput(val resId: Int, private var enteredText: String) {
    init {
        enteredText = enteredText.trim()
    }

    fun isBlank(): Boolean {
        return enteredText.isBlank()
    }

    fun getEnteredText(): String {
        return enteredText
    }

    companion object {
        fun validateEmailInput(input: LoginInput, res: ResourceProvider): LoginValidation {
            if (input.isBlank() || !input.getEnteredText().contains(
                            "@") || input.getEnteredText().length < 3) {
                return LoginValidation(input, false, res.getString(R.string.error_invalid_username))
            } else return LoginValidation(input)
        }

        fun validatePasswordInput(input: LoginInput, res: ResourceProvider): LoginValidation {
            if (input.isBlank() || input.getEnteredText().length < 4) {
                return LoginValidation(input, false, res.getString(R.string.error_invalid_password))
            } else return LoginValidation(input)
        }
    }
}