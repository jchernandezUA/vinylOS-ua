@file:Suppress("SpellCheckingInspection")

package com.uniandes.vynilos.common

import android.content.Intent
import android.os.Build
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

val TIME_SERVER_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

inline fun <T> resultOrError(block: () -> T): DataState<T> {
    return try {
        DataState.Success(block())
    } catch (e: Exception) {
        DataState.Error(e)
    }
}

inline fun <reified T : Parcelable> Intent.getSafeParcelableExtra(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableExtra(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getParcelableExtra(key) as? T
    }
}

fun String.convertDateToTimestamp(
    pattern: String = TIME_SERVER_PATTERN
): Long {
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("UTC")
    val date = format.parse(this)
    return date?.time ?: 0
}


fun Long.timestampToYear(): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return calendar.get(Calendar.YEAR)
}

fun timestampToFormattedString(
    timestamp: Long,
    pattern: String
): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(date)
}

fun fixTimeOffset(timestamp: Long): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp
    val timeZone = TimeZone.getDefault()
    val offsetInMillis = timeZone.rawOffset
    val offsetInHours = -offsetInMillis / (60 * 60 * 1000)
    calendar.add(Calendar.HOUR_OF_DAY, offsetInHours)
    return calendar.timeInMillis
}