package com.storytel.login.feature

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.storytel.login.feature.create.countrypicker.CountryPickerArguments
import com.storytel.login.feature.login.forgot.ForgotPasswordArguments
import com.storytel.base.util.SingleLiveEvent
import dagger.hilt.android.scopes.FragmentScoped

@FragmentScoped class LoginNavigatorViewModel() : ViewModel(), LoginNavigator {
    override fun onLoginSelected() {

    }

    override fun onLoggedIn() {

    }

    override fun onResetPasswordSelected(args : ForgotPasswordArguments) {

    }

    override fun onCreateAccountSelected() {

    }

    override fun onDisplayCountryPicker(args : CountryPickerArguments) {

    }


    private val _showLogin = SingleLiveEvent<Any>()
    val showLogin: LiveData<Any>
        get() = _showLogin

    private val _showResetPassword =
            SingleLiveEvent<ForgotPasswordArguments>()
    val showResetPassword: LiveData<ForgotPasswordArguments>
        get() = _showResetPassword

    private val _showCreateAccountSelected = SingleLiveEvent<Any>()
    val showCreateAccountSelected: LiveData<Any>
        get() = _showCreateAccountSelected

    private val _showCountryPicker =
            SingleLiveEvent<CountryPickerArguments>()
    val showCountryPicker: LiveData<CountryPickerArguments>
        get() = _showCountryPicker


}