package com.storytel.login.util

import com.storytel.base.util.ApiResponse
import com.storytel.base.util.ApiSuccess
import com.storytel.base.util.Resource
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import retrofit2.Response

object PublishResponse {
    fun <T> publish(response: Response<T>, publish: PublishSubject<Resource<ApiResponse<T>>>) {
        val apiRes = ApiResponse.create(response)
        if (apiRes is ApiSuccess) {
            publish.onNext(Resource.success(apiRes))
        } else {
            publish.onNext(Resource.error(apiRes))
        }
    }

    fun <T> publish(error: Throwable, publish: PublishSubject<Resource<ApiResponse<T>>>) {
        publish.onNext(Resource.error(ApiResponse.create(error)))
    }

    fun <T> errorHandler(
            publish: PublishSubject<Resource<ApiResponse<T>>>): io.reactivex.functions.Consumer<in Throwable> {
        return Consumer { throwable ->
            publish(throwable, publish)
        }
    }
}