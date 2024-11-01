package com.uniandes.vynilos.data.model

import com.uniandes.vynilos.data.remote.entity.ArtistResponse


data class Artist(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
)

fun ArtistResponse.DTO() = Artist(
    id,
    name,
    image,
    description
)

fun List<ArtistResponse>.DTO() = map { it.DTO() }