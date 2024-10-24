package com.uniandes.vynilos.common

inline fun <T> resultOrError(block: () -> T): DataState<T> {
    return try {
        DataState.Success(block())
    } catch (e: Exception) {
        DataState.Error(e)
    }
}