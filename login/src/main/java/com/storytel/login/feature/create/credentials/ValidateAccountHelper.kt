package com.storytel.login.feature.create.credentials

import androidx.lifecycle.MutableLiveData
import com.facebook.AccessToken
import com.storytel.base.util.ApiResponse
import com.storytel.base.util.ApiSuccess
import com.storytel.login.pojo.LoginInput
import com.storytel.login.pojo.LoginValidation
import com.storytel.login.pojo.ValidateSignUpResponse
import com.storytel.base.util.ResourceProvider
import com.storytel.base.util.ErrorHelper
import com.storytel.base.util.Resource
import com.storytel.base.util.Status
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class ValidateAccountHelper(private val repository: CreateAccountRepository,
                            private val res: ResourceProvider,
                            private val compositeDisposable: CompositeDisposable,
                            private val deviceLocale: String, private val listener: ValidateAccountListener) {

    val validateAccountResponseLiveData = MutableLiveData<ValidateAccountUiModel>()
    private val publishValidateAccountResponse: PublishSubject<Resource<ApiResponse<ValidateSignUpResponse>>> =
            PublishSubject.create<Resource<ApiResponse<ValidateSignUpResponse>>>()

    init {
        compositeDisposable.add(publishValidateAccountResponse.subscribeOn(Schedulers.io()).map { result ->
            {
                val model = convertToUiModel(result.status, result.data)
                if (model.isValid) {
                    listener.onAccountIsValid()
                }
                model
            }
        }.observeOn(AndroidSchedulers.mainThread()).subscribe { uiModel ->
            validateAccountResponseLiveData.value = uiModel.invoke()
        })
    }

    private fun convertToUiModel(status: Status,
                                 data: ApiResponse<ValidateSignUpResponse>?): ValidateAccountUiModel {
        when (status) {
            Status.SUCCESS -> {
                val response = data as ApiSuccess
                val errorMessage = response.body.failReason ?: ""
                return ValidateAccountUiModel(isValid = response.body.valid, errorMessage = errorMessage)
            }

            Status.ERROR   -> {
                return ValidateAccountUiModel(errorMessage = ErrorHelper.getGenericErrorMessage(res, data))
            }

            Status.LOADING -> {
                return ValidateAccountUiModel(isLoading = true)
            }
        }
    }

    fun validateSignUpParameters(email: LoginInput, password: LoginInput, bestGuessCountry: String) {
        val emailValidation = LoginInput.validateEmailInput(email, res)
        val passwordValidation = LoginInput.validatePasswordInput(password, res)
        if (emailValidation.isValid && passwordValidation.isValid) {
            if (isNotLoading()) {
                val signUpFields =
                        SignUpFields(email.getEnteredText(), password.getEnteredText(), bestGuessCountry,
                                deviceLocale)
                repository.validateSignUpParameters(signUpFields, compositeDisposable,
                        publishValidateAccountResponse)
            }
        } else {
            val errorInput = ArrayList<LoginValidation>()
            if (!emailValidation.isValid) {
                errorInput.add(emailValidation)
            }
            if (!passwordValidation.isValid) {
                errorInput.add(passwordValidation)
            }
            validateAccountResponseLiveData.value = ValidateAccountUiModel(inputErrorValidation = errorInput)
        }
    }

    fun validateFacebookSignUpParameters(accessToken: AccessToken, bestGuessCountryForUser: String) {
        if (isNotLoading() && accessToken.token != null) {
            repository.validateFacebookSignUpParameters(accessToken.token, bestGuessCountryForUser,
                    deviceLocale, compositeDisposable, publishValidateAccountResponse)
        }
    }

    private fun isNotLoading(): Boolean {
        val v = validateAccountResponseLiveData.value
        return (v == null || !v.isLoading)
    }
}

interface ValidateAccountListener {
    fun onAccountIsValid()
}