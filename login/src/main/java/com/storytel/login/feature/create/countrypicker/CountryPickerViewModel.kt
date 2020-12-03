package com.storytel.login.feature.create.countrypicker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.storytel.login.pojo.Country
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped class CountryPickerViewModel @Inject constructor(): ViewModel() {
    val uiModel = MutableLiveData<CountryPickerUiModel>()

    fun setArgumentValues(countries: Array<Country>, selectedCountry: String) {
        if (uiModel.value == null) {
            uiModel.value = CountryPickerUiModel(selectedCountry, countries,
                    findIndexOfSelectedCountry(countries, selectedCountry))
        }
    }

    fun uiModel(): LiveData<CountryPickerUiModel> {
        return uiModel
    }

    fun onCountrySelected(country: Country, index: Int) {
        val current = uiModel.value
        current?.let {
            uiModel.value = CountryPickerUiModel(country.iso ?: "", it.countries, index)
        }
    }

    private fun findIndexOfSelectedCountry(countries: Array<Country>, selectedCountryIso: String): Int {
        return countries.indexOfFirst { country ->
            country.iso != null && country.iso == selectedCountryIso
        }
    }
}