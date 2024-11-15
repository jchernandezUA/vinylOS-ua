package com.uniandes.vynilos.common

import retrofit2.HttpException
import java.io.IOException

sealed class DataState<out T> {
    data object Idle : DataState<Nothing>()
    data object Loading : DataState<Nothing>()
    data class Success<out T> (val data: T): DataState<T>()
    class Error(val error: DataError): DataState<Nothing>() {
        constructor(exception: Exception) : this(exception.let {
            when(exception) {
                is IOException  -> {
                    DataError(
                        message = exception.message?:"No connection available")
                }
                is HttpException -> {
                    DataError(
                        message = exception.message?:"Not defined error"
                    )
                }
                else -> {
                    DataError(
                        exception.message?:"Not defined error"
                    )
                }
            }
        })
    }

    fun getDataOrNull(): T? = if (this is Success) data else null
}