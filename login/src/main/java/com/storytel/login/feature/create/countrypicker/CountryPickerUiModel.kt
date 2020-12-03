package com.storytel.login.feature.create.countrypicker

import com.storytel.login.pojo.Country

data class CountryPickerUiModel(val selectedCountry : String, val countries: Array<Country>, val
indexOfSelectedCountry : Int)