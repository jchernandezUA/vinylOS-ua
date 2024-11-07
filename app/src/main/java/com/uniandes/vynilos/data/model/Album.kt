package com.uniandes.vynilos.data.model

import com.uniandes.vynilos.common.convertDateToTimestamp
import com.uniandes.vynilos.data.remote.entity.AlbumResponse
import java.io.Serializable


data class Album(
    val id: Int,
    val name: String,
    val cover: String,
    val releaseDate: Long,
    val description: String,
    val genre: String,
    val recordLabel: String
): Serializable


fun AlbumResponse.DTO() = Album(
    id = id,
    name = name,
    cover = cover,
    description = description,
    genre = genre,
    recordLabel = recordLabel,
    releaseDate = releaseDate.convertDateToTimestamp()
)

fun List<AlbumResponse>.DTO() = map { it.DTO() }