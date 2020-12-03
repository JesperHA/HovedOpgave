package com.storytel.base.util

import com.storytel.base.R

object ErrorHelper {
    fun getGenericErrorMessage(res: ResourceProvider, data: ApiResponse<*>?): String {
        val errorMessage: String;
        if (data is ApiError) {
            errorMessage =
                    getErrorFromApiError(res, data)
        } else if (data is ApiConnectionError) {
            errorMessage = "connection failed - (no translation!)"
        } else {
            errorMessage = res.getString(R.string.error_something_went_wrong)
        }
        return errorMessage
    }

    private fun getErrorFromApiError(res: ResourceProvider, data: ApiError<*>): String {
        if (data.errorMessage.isNotBlank()) {
            return data.errorMessage
        } else {
            return res.getString(R.string.error_something_went_wrong)
        }
    }
}