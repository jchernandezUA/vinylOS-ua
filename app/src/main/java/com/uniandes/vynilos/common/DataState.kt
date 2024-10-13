package com.uniandes.vynilos.common

import retrofit2.HttpException
import retrofit2.Retrofit

class DataState<T>(val data: T? = null,
                   val error: DataError? = null,
                   var status: DataStatus = DataStatus.LOADING
)  {

    companion object {
        fun <T> success(data: T): DataState<T> =
            DataState(data = data, status = DataStatus.SUCCESS)
        fun <T> error(exception: Exception): DataState<T> =
            when(exception) {
                is HttpException -> {
                    DataState(error = DataError(
                        message = exception.message?:"Not defined error",
                        statusCode = exception.code()
                    ),
                        status = DataStatus.ERROR
                    )
                }
                else -> {
                    DataState(error = DataError(exception.message?:"Not defined error"), status = DataStatus.ERROR)
                }
            }
    }

    override fun toString(): String {
        return "Status: ${status.name}, Data: ${data?.toString()}, Error: ${error?.message}"
    }

}

enum class DataStatus {
    LOADING,
    SUCCESS,
    ERROR
}