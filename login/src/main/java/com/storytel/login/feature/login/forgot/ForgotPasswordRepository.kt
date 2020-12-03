package com.storytel.login.feature.login.forgot

import com.storytel.base.util.ApiEmptyResponse
import com.storytel.base.util.ApiResponse
import com.storytel.base.util.ApiSuccess
import com.storytel.login.api.LoginWebService
import com.storytel.base.util.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Repository that handles Login.
 */
class ForgotPasswordRepository @Inject constructor(private val api: LoginWebService) {

    fun resetPassword(email: String, compositeDisposable: CompositeDisposable,
                      publish: PublishSubject<Resource<ApiResponse<Any>>>) {
        compositeDisposable.add(api.forgotPassword(email).doOnSubscribe {
            publish.onNext(Resource.loading())
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ response ->
            val apiRes = ApiResponse.create(response)
            if (apiRes is ApiSuccess) {
                publish.onNext(Resource.success(apiRes))
            } else if (apiRes is ApiEmptyResponse) {
                publish.onNext(Resource.success(null))
            } else {
                publish.onNext(Resource.error(apiRes))
            }
        }, { throwable ->
            publish.onNext(
                    Resource.error(ApiResponse.create(throwable)))
        }))
    }
}
