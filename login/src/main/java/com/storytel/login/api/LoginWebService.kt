package com.storytel.login.api

import com.storytel.login.pojo.AvailableCountriesResponse
import com.storytel.login.pojo.LoginResponse
import com.storytel.login.pojo.SocialLoginResponse
import com.storytel.login.pojo.ValidateSignUpResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginWebService {
    @GET("/api/login.action?m=1") fun login(@Query("uid") userId: String, @Query("pwd")
    password: String): Observable<Response<LoginResponse>>

    @GET("/api/socialLogin.action") fun facebookLogin(@Query("socialId") socialId: String, @Query("fbToken")
    fbToken: String): Observable<Response<SocialLoginResponse>>

    @POST("/api/v2/auth/forgot") @FormUrlEncoded fun forgotPassword(@Field("email")
                                                                    email: String): Observable<Response<Any>>

    @FormUrlEncoded @POST("/api/v2/signUp/validateParameters") fun validateSignUpParameters(@Field("email")
                                                                                            email: String,
                                                                                            @Field("password")
                                                                                            password: String,
                                                                                            @Field("signUpCountryIso")
                                                                                            signUpCountryIso: String,
                                                                                            @Field("locale")
                                                                                            locale: String): Observable<Response<ValidateSignUpResponse>>

    @FormUrlEncoded
    @POST("/api/v2/facebookSignUp/validateParameters")
    fun validateFacebookSignUpParameters(@Field("fbToken") fbToken: String,
                                                  @Field("signUpCountryIso") signUpCountryIso: String,
                                                  @Field("locale")
                                                  locale: String): Observable<Response<ValidateSignUpResponse>>

    @GET("/api/v2/signUp/availableCountries") fun getAvailableCountries(@Query("locale")
                                                                        locale: String): Observable<Response<AvailableCountriesResponse>>
}