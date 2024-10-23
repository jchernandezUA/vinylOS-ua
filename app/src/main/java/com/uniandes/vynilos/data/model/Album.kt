package com.uniandes.vynilos.data.model

import com.uniandes.vynilos.data.remote.entity.AlbumResponse


data class Album(
    val id: Int,
    val name: String,
    val cover: String,
    val description: String,
)


fun AlbumResponse.DTO() = Album(
    id,
    name,
    cover,
    description
)

fun List<AlbumResponse>.DTO() = map { it.DTO() }