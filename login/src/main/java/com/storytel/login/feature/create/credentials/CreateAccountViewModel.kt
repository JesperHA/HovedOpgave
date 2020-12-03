package com.storytel.login.feature.create.credentials

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.storytel.login.feature.create.credentials.country.AvailableCountriesHelper
import com.storytel.login.feature.create.credentials.country.CountriesUiModel
import com.storytel.login.pojo.Country
import com.storytel.login.pojo.LoginInput
import com.storytel.base.util.ResourceProvider
import com.storytel.base.util.SingleLiveEvent
import dagger.hilt.android.scopes.FragmentScoped
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Named

/**
 * Fetches available countries and the best guess country based on device locale. The best guess country
 * and the entered mail and password are then validated by the api when users submits the values. If valid,
 * the app then displays a list of countries and a preselected country
 */
@FragmentScoped class CreateAccountViewModel @Inject constructor(
        private val repository: CreateAccountRepository, private val res: ResourceProvider,
        private val compositeDisposable: CompositeDisposable,
        @Named("DeviceLocale") private val deviceLocale: String): ViewModel() {

    private val availableCountriesHelper =
            AvailableCountriesHelper(repository, res, compositeDisposable, deviceLocale)

    private val validateAccountHelper =
            ValidateAccountHelper(repository, res, compositeDisposable, deviceLocale,
                    object: ValidateAccountListener {
                        override fun onAccountIsValid() {
                            val countriesUiModel = availableCountriesHelper.getCountriesUiModel()
                            if (countriesUiModel.countries.size == 1) {
                                onCountryAutoSelected(countriesUiModel.countries.get(0))
                            } else {
                                showCountryPicker(countriesUiModel)
                            }
                        }
                    })
    private val displayCountryPicker =
            SingleLiveEvent<CountriesUiModel>()
    private val countryAutoSelected = SingleLiveEvent<Country>()

    fun validateAccountUiModel(): LiveData<ValidateAccountUiModel> {
        return validateAccountHelper.validateAccountResponseLiveData
    }

    fun countriesUiModel(): LiveData<CountriesUiModel> {
        return availableCountriesHelper.countriesResponseLiveData
    }

    fun attemptCreateAccountWithFacebook(accessToken: AccessToken?) {
        accessToken?.let {
            validateAccountHelper.validateFacebookSignUpParameters(it,
                    availableCountriesHelper.getBestGuessCountryForUser())
        }
    }

    fun attemptCreateAccount(email: LoginInput, password: LoginInput) {
        validateAccountHelper.validateSignUpParameters(email, password,
                availableCountriesHelper.getBestGuessCountryForUser())
    }

    private fun showCountryPicker(uiModel: CountriesUiModel) {
        displayCountryPicker.value = uiModel
    }

    private fun onCountryAutoSelected(country: Country) {
        countryAutoSelected.value = country
    }

    fun displayCountryPicker(): LiveData<CountriesUiModel> {
        return displayCountryPicker
    }

    fun countryAutoSelected(): LiveData<Country> {
        return countryAutoSelected
    }

}