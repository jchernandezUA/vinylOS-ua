package com.uniandes.vynilos.data.model

import android.os.Parcelable
import com.uniandes.vynilos.data.remote.entity.CollectorAlbumResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class CollectorAlbum(
    val id: Int,
    val price: Int,
    val status: String,
    val album : Album?,
    val collector: Collector?
) : Parcelable

fun CollectorAlbumResponse.toModel() = CollectorAlbum(
    id = id,
    price = price,
    status = status,
    album = album?.DTO(),
    collector = collector?.copy(comments = emptyList(), favoritePerformers = emptyList(), collectorAlbums = emptyList())?.toModel()
)

fun List<CollectorAlbumResponse>.toModelList() = map { it.toModel() }