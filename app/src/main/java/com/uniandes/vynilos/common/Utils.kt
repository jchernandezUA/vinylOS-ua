package com.uniandes.vynilos.common

import android.content.Intent
import android.os.Build
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

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

fun String.convertDateToTimestamp(): Long {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("UTC")
    val date = format.parse(this)
    return date?.time ?: 0
}


fun Long.timestampToYear(): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return calendar.get(Calendar.YEAR)
}