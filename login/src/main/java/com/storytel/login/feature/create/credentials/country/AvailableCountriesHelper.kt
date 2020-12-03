package com.storytel.login.feature.create.credentials.country

import androidx.lifecycle.MutableLiveData
import com.storytel.base.util.ApiResponse
import com.storytel.base.util.ApiSuccess
import com.storytel.login.feature.create.credentials.CreateAccountRepository
import com.storytel.login.pojo.AvailableCountriesResponse
import com.storytel.base.util.ResourceProvider
import com.storytel.base.util.ErrorHelper
import com.storytel.base.util.Resource
import com.storytel.base.util.Status
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.Collections

class AvailableCountriesHelper(private val repository: CreateAccountRepository,
                               private val res: ResourceProvider,
                               private val compositeDisposable: CompositeDisposable,
                               private val deviceLocale: String) {

    val countriesResponseLiveData = MutableLiveData<CountriesUiModel>()
    private val publishCountriesResponse: PublishSubject<Resource<ApiResponse<AvailableCountriesResponse>>> =
            PublishSubject.create<Resource<ApiResponse<AvailableCountriesResponse>>>()

    init {
        compositeDisposable.add(publishCountriesResponse.subscribeOn(Schedulers.io()).map { result ->
            {
                convertToUiModel(result.status, result.data)
            }
        }.observeOn(AndroidSchedulers.mainThread()).subscribe { uiModel ->
            countriesResponseLiveData.value = uiModel.invoke()
        })
        fetchAvailableCountriesForUser()
    }

    private fun convertToUiModel(status: Status,
                                 data: ApiResponse<AvailableCountriesResponse>?): CountriesUiModel {
        when (status) {
            Status.SUCCESS -> {
                val apiResponse = data as ApiSuccess
                val response = apiResponse.body
                val availableCountries = response.availableCountries ?: Collections.emptyList()
                val bestGuessCountry: String = response.bestGuessCountry ?: ""
                val countries = response.countries ?: arrayOf()
                return CountriesUiModel(
                        availableCountries = availableCountries, bestGuessCountry = bestGuessCountry,
                        countries = countries)
            }

            Status.ERROR   -> {
                return CountriesUiModel(
                        errorMessage = ErrorHelper.getGenericErrorMessage(res, data))
            }

            Status.LOADING -> {
                return CountriesUiModel(
                        isLoading = true)
            }
        }
    }

    fun fetchAvailableCountriesForUser() {
        val v = countriesResponseLiveData.value
        if (v == null || !v.isLoading) {
            repository.fetchAvailableCountriesForUser(deviceLocale, compositeDisposable,
                    publishCountriesResponse)
        }
    }

    fun getBestGuessCountryForUser(): String {
        val bestGuessCountry: String
        val v = countriesResponseLiveData.value
        if (v != null) {
            bestGuessCountry = v.bestGuessCountry
        } else {
            bestGuessCountry = ""
        }
        return bestGuessCountry
    }

    fun getCountriesUiModel() : CountriesUiModel = countriesResponseLiveData.value ?: CountriesUiModel()
}