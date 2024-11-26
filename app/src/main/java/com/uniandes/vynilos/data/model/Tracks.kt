package com.uniandes.vynilos.data.model

import android.os.Parcelable
import com.uniandes.vynilos.data.remote.entity.TracksResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tracks(
    val id: Int? = null,
    val name: String,
    val duration: String
) : Parcelable

fun TracksResponse.DTO() = Tracks(
    id = id,
    name = name,
    duration = duration
)

fun List<TracksResponse>.DTO() = map { it.DTO() }
