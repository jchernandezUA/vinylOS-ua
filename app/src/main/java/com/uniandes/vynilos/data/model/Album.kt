package com.uniandes.vynilos.data.model

import android.os.Parcelable
import com.uniandes.vynilos.common.convertDateToTimestamp
import com.uniandes.vynilos.data.remote.entity.AlbumResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class Album(
    val id: Int,
    val name: String,
    val cover: String,
    val releaseDate: Long,
    val description: String,
    val genre: String,
    val recordLabel: String
): Parcelable


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