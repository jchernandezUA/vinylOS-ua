package com.uniandes.vynilos.data.remote.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class CollectorResponse(
    val id: Int,
    val name: String,
    val telephone: String,
    val email: String,
    val comments: List<CommentResponse> = emptyList(),
    val favoritePerformers: List<PerformerResponse> = emptyList(),
    val collectorAlbums: List<CollectorAlbumResponse> = emptyList(),

)

data class CollectorAlbumResponse(
    val id: Int,
    val price: Int,
    val status: String,
    val album : AlbumResponse?,
    val collector : CollectorResponse?
)

@Parcelize
data class CollectorDTO(
    val id: Int
): Parcelable


