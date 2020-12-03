package com.storytel.login.feature.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.storytel.login.R
import com.storytel.base.util.ApiError
import com.storytel.base.util.ApiResponse
import com.storytel.login.pojo.LoginInput
import com.storytel.login.pojo.LoginResponse
import com.storytel.login.pojo.LoginValidation
import com.storytel.login.pojo.SocialLoginResponse
import com.storytel.base.util.ResourceProvider
import com.storytel.base.util.ErrorHelper
import com.storytel.base.util.Resource
import com.storytel.base.util.Status
import dagger.hilt.android.scopes.FragmentScoped
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.net.HttpURLConnection
import javax.inject.Inject

@FragmentScoped class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository,
                                                         private val res: ResourceProvider,
                                                         private val compositeDisposable: CompositeDisposable):
        ViewModel() {

    private val loginResponseLiveData = MutableLiveData<UserLoginUiModel>()
    private val publishUserLoginResponse: PublishSubject<Resource<ApiResponse<LoginResponse>>> =
            PublishSubject.create<Resource<ApiResponse<LoginResponse>>>()
    private val publishSocialLoginResponse: PublishSubject<Resource<ApiResponse<SocialLoginResponse>>> =
            PublishSubject.create<Resource<ApiResponse<SocialLoginResponse>>>()

    init {
        compositeDisposable.add(publishUserLoginResponse.subscribeOn(Schedulers.io()).map { result ->
            {
                convertToUiModel(result.status, result.data)
            }
        }.observeOn(AndroidSchedulers.mainThread()).subscribe { uiModel ->
            loginResponseLiveData.value = uiModel.invoke()
        })
        compositeDisposable.add(publishSocialLoginResponse.subscribeOn(Schedulers.io()).map { result ->
            {
                convertToUiModel(result.status, result.data)
            }
        }.observeOn(AndroidSchedulers.mainThread()).subscribe { uiModel ->
            loginResponseLiveData.value = uiModel.invoke()
        })
    }

    fun uiModel(): LiveData<UserLoginUiModel> {
        return loginResponseLiveData
    }

    private fun convertToUiModel(status: Status, data: ApiResponse<*>?): UserLoginUiModel {
        when (status) {
            Status.SUCCESS -> {
                return UserLoginUiModel(isLoggedIn = true)
            }

            Status.ERROR   -> {
                return convertErrorToUiModel(data)
            }

            Status.LOADING -> {
                return UserLoginUiModel(isLoading = true)
            }
        }
    }

    private fun convertErrorToUiModel(data: ApiResponse<*>?): UserLoginUiModel {
        val errorMessage: String;
        if (data is ApiError) {
            errorMessage = getApiErrorFromHttpResponseCode(data.httpResponseCode)
        } else {
            errorMessage = ErrorHelper.getGenericErrorMessage(res, data)
        }
        return UserLoginUiModel(errorMessage = errorMessage)
    }

    private fun getApiErrorFromHttpResponseCode(httpResponseCode: Int): String {
        when (httpResponseCode) {
            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                return res.getString(R.string.error_invalid_password)
            }

            else                                -> return res.getString(R.string.error_something_went_wrong)
        }
    }

    fun attemptLogin(email: LoginInput, password: LoginInput) {
        val emailValidation = LoginInput.validateEmailInput(email, res)
        val passwordValidation = LoginInput.validatePasswordInput(password, res)
        if (emailValidation.isValid && passwordValidation.isValid) {
            val v = loginResponseLiveData.value
            if (v == null || !v.isLoading) {
                loginRepository.performLogin(email.getEnteredText(), password.getEnteredText(),
                        compositeDisposable, publishUserLoginResponse)
            }
        } else {
            val errorInput = ArrayList<LoginValidation>()
            if (!emailValidation.isValid) {
                errorInput.add(emailValidation)
            }
            if (!passwordValidation.isValid) {
                errorInput.add(passwordValidation)
            }
            loginResponseLiveData.value = UserLoginUiModel(inputErrorValidation = errorInput)
        }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

    fun attemptLoginWithFacebook(accessToken: AccessToken?) {
        val v = loginResponseLiveData.value
        if (v == null || !v.isLoading) {
            accessToken?.let {
                loginRepository.facebookSocialLogin(it, compositeDisposable, publishSocialLoginResponse)
            }
        }
    }

    fun clearErrors() {
        loginResponseLiveData.value.let {
            if (it != null && (it.inputErrorValidation.isNotEmpty() || it.errorMessage.isNotBlank())) {
                loginResponseLiveData.value = UserLoginUiModel(it.isLoading, "", it.isLoggedIn)
            }
        }
    }
}