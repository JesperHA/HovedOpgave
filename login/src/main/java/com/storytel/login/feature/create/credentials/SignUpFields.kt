package com.storytel.login.feature.create.credentials

data class SignUpFields(val email: String, val password: String, val signUpCountryIso: String,
                        val locale: String)