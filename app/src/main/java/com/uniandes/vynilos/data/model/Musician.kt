package com.uniandes.vynilos.data.model

import android.os.Parcelable
import com.uniandes.vynilos.data.remote.entity.MusicianResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class Musician(
    val id: Int,
    val name: String,
    val instrument: String
): Parcelable

fun MusicianResponse.DTO() = Musician(
    id = id,
    name = name,
    instrument = instrument
)

fun List<MusicianResponse>.DTO() = map { it.DTO()  }

