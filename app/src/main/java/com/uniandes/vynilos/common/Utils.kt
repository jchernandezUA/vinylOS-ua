package com.uniandes.vynilos.common

import android.content.Intent
import android.os.Build
import java.io.Serializable

inline fun <T> resultOrError(block: () -> T): DataState<T> {
    return try {
        DataState.Success(block())
    } catch (e: Exception) {
        DataState.Error(e)
    }
}

inline fun <reified T : Serializable> Intent.getSafeSerializableExtra(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getSerializableExtra(key) as? T
    }
}