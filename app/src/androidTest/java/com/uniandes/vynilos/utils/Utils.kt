package com.uniandes.vynilos.utils

import android.content.Context
import androidx.annotation.StringRes

fun getString(context: Context, @StringRes id: Int): String = context.getString(id)
