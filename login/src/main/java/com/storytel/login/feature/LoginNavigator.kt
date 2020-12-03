package com.storytel.login.feature

import com.storytel.login.feature.create.countrypicker.CountryPickerArguments
import com.storytel.login.feature.login.forgot.ForgotPasswordArguments

interface LoginNavigator {
    fun onLoginSelected()
    fun onLoggedIn()
    fun onResetPasswordSelected(args : ForgotPasswordArguments)
    fun onCreateAccountSelected()
    fun onDisplayCountryPicker(args : CountryPickerArguments)
}