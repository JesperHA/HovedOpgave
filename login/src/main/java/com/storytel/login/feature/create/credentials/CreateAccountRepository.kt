package com.storytel.login.feature.create.credentials

import com.storytel.base.util.ApiEmptyResponse
import com.storytel.base.util.ApiResponse
import com.storytel.base.util.ApiSuccess
import com.storytel.login.api.LoginWebService
import com.storytel.login.feature.create.credentials.country.CountryUtil
import com.storytel.login.pojo.AvailableCountriesResponse
import com.storytel.login.pojo.ValidateSignUpResponse
import com.storytel.login.util.PublishResponse
import com.storytel.base.util.Resource
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import retrofit2.Response
import javax.inject.Inject

/**
 *
 */
class CreateAccountRepository @Inject constructor(private val api: LoginWebService,
                                                  private val countryUtil: CountryUtil) {

    fun validateSignUpParameters(signUpFields: SignUpFields, compositeDisposable: CompositeDisposable,
                                 publish: PublishSubject<Resource<ApiResponse<ValidateSignUpResponse>>>) {
        validateSignUpResponse(api.validateSignUpParameters(signUpFields.email, signUpFields.password,
                signUpFields.signUpCountryIso, signUpFields.locale), compositeDisposable, publish)
    }

    fun validateFacebookSignUpParameters(fbToken: String, bestGuessCountryIso: String, deviceLocale: String,
                                         compositeDisposable: CompositeDisposable,
                                         publish: PublishSubject<Resource<ApiResponse<ValidateSignUpResponse>>>) {
        validateSignUpResponse(
                api.validateFacebookSignUpParameters(fbToken, bestGuessCountryIso, deviceLocale),
                compositeDisposable, publish)
    }

    private fun validateSignUpResponse(apiObservable: Observable<Response<ValidateSignUpResponse>>,
                                       compositeDisposable: CompositeDisposable,
                                       publish: PublishSubject<Resource<ApiResponse<ValidateSignUpResponse>>>) {
        compositeDisposable.add(apiObservable.doOnSubscribe {
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
                    PublishResponse.publish(throwable, publish)
                }))
    }

    fun fetchAvailableCountriesForUser(locale: String, compositeDisposable: CompositeDisposable,
                                       publish: PublishSubject<Resource<ApiResponse<AvailableCountriesResponse>>>) {

        compositeDisposable.add(api.getAvailableCountries(locale).doOnSubscribe {
            publish.onNext(Resource.loading())
        }.subscribeOn(Schedulers.io()).map { result ->
            countryUtil.setDisplayNameAndFlagRes(result)
            result
        }.observeOn(AndroidSchedulers.mainThread()).subscribe({ response ->
            PublishResponse.publish(response, publish)
        }, { throwable ->
            PublishResponse.publish(throwable, publish)
        }))
    }

}
