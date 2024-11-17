package com.uniandes.vynilos.data.model

import android.os.Parcelable
import com.uniandes.vynilos.data.remote.entity.CollectorAlbumResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class CollectorAlbum(
    val id: Int,
    val price: Double,
    val status: String
) : Parcelable

fun CollectorAlbumResponse.toModel() = CollectorAlbum(
    id = id,
    price = price,
    status = status
)
