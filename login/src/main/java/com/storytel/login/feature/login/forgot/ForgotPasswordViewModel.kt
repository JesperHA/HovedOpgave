package com.storytel.login.feature.login.forgot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.storytel.login.R
import com.storytel.base.util.ApiError
import com.storytel.base.util.ApiResponse
import com.storytel.login.pojo.LoginInput
import com.storytel.login.pojo.LoginValidation
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

@FragmentScoped class ForgotPasswordViewModel @Inject constructor(
        private val repository: ForgotPasswordRepository, private val res: ResourceProvider,
        private val compositeDisposable: CompositeDisposable): ViewModel() {

    private val resetPasswordResponseLiveData = MutableLiveData<ForgotUiModel>()
    private val publishResetPasswordResponse: PublishSubject<Resource<ApiResponse<Any>>> =
            PublishSubject.create<Resource<ApiResponse<Any>>>()

    init {
        compositeDisposable.add(publishResetPasswordResponse.subscribeOn(Schedulers.io()).map { result ->
            {
                convertToUiModel(result.status, result.data)
            }
        }.observeOn(AndroidSchedulers.mainThread()).subscribe { uiModel ->
            resetPasswordResponseLiveData.value = uiModel.invoke()
        })
    }

    fun uiModel(): LiveData<ForgotUiModel> {
        return resetPasswordResponseLiveData
    }

    private fun convertToUiModel(status: Status, data: ApiResponse<*>?): ForgotUiModel {
        when (status) {
            Status.SUCCESS -> {
                return ForgotUiModel(hasResetPassword = true)
            }

            Status.ERROR   -> {
                return convertErrorToUiModel(data)
            }

            Status.LOADING -> {
                return ForgotUiModel(isLoading = true)
            }
        }
    }

    private fun convertErrorToUiModel(data: ApiResponse<*>?): ForgotUiModel {
        val errorMessage: String;
        if (data is ApiError) {
            errorMessage = getApiErrorFromHttpResponseCode(data.httpResponseCode)
        } else {
            errorMessage = ErrorHelper.getGenericErrorMessage(res, data)
        }
        return ForgotUiModel(errorMessage = errorMessage)
    }

    private fun getApiErrorFromHttpResponseCode(httpResponseCode: Int): String {
        when (httpResponseCode) {
            HttpURLConnection.HTTP_BAD_REQUEST -> {
                return res.getString(R.string.alert_message_problem_resetting_password_no_account_found)
            }

            else                               -> return res.getString(R.string.error_something_went_wrong)
        }
    }

    fun resetPassword(email: LoginInput) {
        val emailValidation = LoginInput.validateEmailInput(email, res)
        if (emailValidation.isValid) {
            val v = resetPasswordResponseLiveData.value
            if (v == null || !v.isLoading) {
                repository.resetPassword(email.getEnteredText(), compositeDisposable,
                        publishResetPasswordResponse)
            }
        } else {
            val errorInput = ArrayList<LoginValidation>()
            if (!emailValidation.isValid) {
                errorInput.add(emailValidation)
            }
            resetPasswordResponseLiveData.value = ForgotUiModel(inputErrorValidation = errorInput)
        }
    }
}