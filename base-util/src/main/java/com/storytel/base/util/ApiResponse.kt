package com.storytel.base.util

import retrofit2.Response

/**
 * Common class used by API responses.
 * @param <T> the type of the response object
</T> */
@Suppress("unused") // T is used in extending classes
sealed class ApiResponse<T> {

    companion object {
        fun <T> create(error: Throwable): ApiConnectionError<T> {
            return ApiConnectionError(error.message ?: "unknown error")
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    ApiEmptyResponse()
                } else {
                    ApiSuccess(body)
                }
            } else {
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                ApiError(errorMsg ?: "unknown error", response.code())
            }
        }
    }
}

/**
 * separate class for HTTP 204 responses so that we can make ApiSuccess's body non-null.
 */
class ApiEmptyResponse<T>: ApiResponse<T>()

data class ApiSuccess<T>(val body: T): ApiResponse<T>()

data class ApiError<T>(val errorMessage: String, val httpResponseCode: Int): ApiResponse<T>()

data class ApiConnectionError<T>(val errorMessage: String): ApiResponse<T>()