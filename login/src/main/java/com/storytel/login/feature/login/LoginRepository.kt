package com.storytel.login.feature.login

import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.storytel.base.util.ApiResponse
import com.storytel.base.util.ApiSuccess
import com.storytel.login.api.LoginWebService
import com.storytel.login.data.UserCredentialStore
import com.storytel.login.encryption.PasswordCrypto
import com.storytel.login.pojo.LoginResponse
import com.storytel.login.pojo.SocialLoginResponse
import com.storytel.base.util.Resource
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles Login.
 */
@Singleton class LoginRepository @Inject constructor(private val api: LoginWebService,
                                                     private val userCredentialStore: UserCredentialStore,
                                                     private val crypto: PasswordCrypto) {

    private fun encrypt(text: String): Observable<String> {
        return Observable.fromCallable {
            crypto.encryptPassword(text)
        }
    }

    fun performLogin(email: String, pwd: String, compositeDisposable: CompositeDisposable,
                     publish: PublishSubject<Resource<ApiResponse<LoginResponse>>>) {
        compositeDisposable.add(encrypt(pwd).doOnSubscribe {
            publish.onNext(Resource.loading())
        }.flatMap { encryptedPassword ->
            api.login(email, encryptedPassword)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ response ->
            val apiRes = ApiResponse.create(response)
            if (apiRes is ApiSuccess && !hasErrorStatus(apiRes.body.result)) {
                userCredentialStore.save(apiRes.body)
                publish.onNext(Resource.success(apiRes))
            } else {
                publish.onNext(Resource.error(apiRes))
            }
        }, { throwable ->
            publish.onNext(
                    Resource.error(ApiResponse.create(throwable)))
        }))
    }

    fun facebookSocialLogin(accessToken: AccessToken, compositeDisposable: CompositeDisposable,
                            publish: PublishSubject<Resource<ApiResponse<SocialLoginResponse>>>) {
        publish.onNext(Resource.loading())
        val meReq = GraphRequest.newMeRequest(accessToken) { user, graphResponse ->
            if (user?.optString("id") != null) {
                val socialId = user.optString("id")
                facebookSocialLogin(socialId, accessToken, compositeDisposable, publish)
            } else {
                publish.onNext(Resource.error(null))
            }
        }
        meReq.executeAsync()
    }

    private fun facebookSocialLogin(socialId: String, accessToken: AccessToken,
                                    compositeDisposable: CompositeDisposable,
                                    publish: PublishSubject<Resource<ApiResponse<SocialLoginResponse>>>) {
        compositeDisposable.add(
                api.facebookLogin(socialId, accessToken.token).subscribeOn(Schedulers.io()).observeOn(
                        AndroidSchedulers.mainThread()).subscribe({ response ->
                    val apiRes = ApiResponse.create(response)
                    if (apiRes is ApiSuccess && !hasErrorStatus(apiRes.body.result)) {
                        publish.onNext(Resource.success(apiRes))
                    } else {
                        publish.onNext(Resource.error(apiRes))
                    }
                }, { throwable ->
                    publish.onNext(
                            Resource.error(
                                    ApiResponse.create(throwable)))
                }))
    }

    private fun hasErrorStatus(result : String?): Boolean {
        result?.let{
            return ("error") == result
        }
        return false
    }
}
