package com.storytel.login.feature.create.countrypicker

import com.storytel.login.pojo.Country

data class CountryPickerArguments(val countries: Array<Country>, val bestGuessCountry: String)