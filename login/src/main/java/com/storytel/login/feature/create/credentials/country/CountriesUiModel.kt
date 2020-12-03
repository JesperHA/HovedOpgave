package com.storytel.login.feature.create.credentials.country

import com.storytel.login.pojo.Country
import java.util.Collections

data class CountriesUiModel(val isLoading: Boolean = false, val errorMessage: String = "",
                            val availableCountries: List<String> = Collections.emptyList(),
                            val bestGuessCountry: String = "",
                            val countries: Array<Country> = arrayOf())