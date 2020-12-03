package com.storytel.login.feature.create.credentials.country

import com.storytel.login.R
import com.storytel.login.pojo.AvailableCountriesResponse
import com.storytel.login.pojo.Country
import retrofit2.Response
import java.util.Locale

class CountryUtil {

    fun setDisplayNameAndFlagRes(response: Response<AvailableCountriesResponse>){
        if (response.isSuccessful && response.body() != null) {
            val countriesResponse = response.body()
            countriesResponse?.let { setCountryDisplayNameAndFlag(it) }
        }
    }

    fun setCountryDisplayNameAndFlag(countriesResponse: AvailableCountriesResponse) {
        countriesResponse.countries?.forEach {
            it.apply {
                displayName = if (iso == null) "" else Locale("", iso).displayName
                if(iso!=null)flagRes = getFlagResourceFromIso(iso)
            }
        }
        sortCountries(countriesResponse)
    }

    fun sortCountries(availableCountriesResponse: AvailableCountriesResponse){
        availableCountriesResponse.countries?.sortWith(object : Comparator<Country>{
            override fun compare(o1: Country?, o2: Country?): Int {
                if(o1?.displayName == null)return 1
                if(o2?.displayName == null)return -1
                val name1 = o1.displayName ?: ""
                val name2 = o2.displayName ?: ""
                return name1.compareTo(name2)
            }
        })
    }

    private fun getFlagResourceFromIso(countryIso: String): Int {
        when (countryIso) {
            "AE" -> return R.drawable.flag_ae
            "DK" -> return R.drawable.flag_dk
            "ES" -> return R.drawable.flag_es
            "FI" -> return R.drawable.flag_fi
            "IN" -> return R.drawable.flag_in
            "NL" -> return R.drawable.flag_nl
            "NO" -> return R.drawable.flag_no
            "PL" -> return R.drawable.flag_pl
            "RU" -> return R.drawable.flag_ru
            "SE" -> return R.drawable.flag_se
            "IS" -> return R.drawable.flag_is
            "TR" -> return R.drawable.flag_tr
            "SA" -> return R.drawable.flag_sa
            "EG" -> return R.drawable.flag_eg
            "KW" -> return R.drawable.flag_kw
            "OM" -> return R.drawable.flag_om
            "BH" -> return R.drawable.flag_bh
            "QA" -> return R.drawable.flag_qa
            "JO" -> return R.drawable.flag_jo
            "LB" -> return R.drawable.flag_lb
            "PS" -> return R.drawable.flag_ps
            "IT" -> return R.drawable.flag_it
            "IQ" -> return R.drawable.flag_iq
            "DZ" -> return R.drawable.flag_dz
            "TN" -> return R.drawable.flag_tn
            "YE" -> return R.drawable.flag_ye
            "MX" -> return R.drawable.flag_mx
            else -> return R.drawable.flag_unknown
        }
    }
}