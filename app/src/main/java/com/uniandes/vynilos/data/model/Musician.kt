package com.uniandes.vynilos.data.model

import com.uniandes.vynilos.data.remote.entity.MusicianResponse

data class Musician(
    val id: Int,
    val name: String,
    val instrument: String
)
fun MusicianResponse.DTO() = Musician(
    id = id,
    name = name,
    instrument = instrument
)

fun List<MusicianResponse>.DTO() = map { it.DTO()  }

