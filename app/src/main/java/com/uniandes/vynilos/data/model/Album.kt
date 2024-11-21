package com.uniandes.vynilos.data.model

import android.os.Parcelable
import com.uniandes.vynilos.common.TIME_SERVER_PATTERN
import com.uniandes.vynilos.common.convertDateToTimestamp
import com.uniandes.vynilos.common.timestampToFormattedString
import com.uniandes.vynilos.data.remote.entity.AlbumRequest
import com.uniandes.vynilos.data.remote.entity.AlbumResponse

import kotlinx.parcelize.Parcelize

@Parcelize
data class Album(
    val id: Int? = null,
    val name: String,
    val cover: String,
    val releaseDate: Long,
    val description: String,
    val genre: String,
    val recordLabel: String,
    val tracks : List<Tracks> = emptyList(),
    val performers : List<Performer> = emptyList(),
    val comments : List<Comment> = emptyList()
): Parcelable


fun AlbumResponse.DTO() = Album(
    id = id,
    name = name,
    cover = cover,
    description = description,
    genre = genre,
    recordLabel = recordLabel,
    releaseDate = releaseDate.convertDateToTimestamp(),
    tracks = tracks?.DTO() ?: emptyList(),
    performers = performers?.DTO()?: emptyList(),
    comments = comments?.DTO()?: emptyList()
)

fun List<AlbumResponse>.DTO() = map { it.DTO() }

fun Album.toDomain() = AlbumRequest(
    name = name,
    cover = cover,
    releaseDate = timestampToFormattedString(
        releaseDate,
        TIME_SERVER_PATTERN
    ),
    description = description,
    genre = genre,
    recordLabel = recordLabel
)