package com.uniandes.vynilos.data.model

import com.uniandes.vynilos.data.remote.entity.MusicianResponse
import java.io.Serializable

data class Musician(
    val id: Int,
    val name: String,
    val instrument: String
): Serializable

fun MusicianResponse.DTO() = Musician(
    id = id,
    name = name,
    instrument = instrument
)

fun List<MusicianResponse>.DTO() = map { it.DTO()  }

