package com.uniandes.vynilos.data.model

import android.os.Parcelable
import com.uniandes.vynilos.data.remote.entity.PerformerResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class Performer(
    val id: Int,
    val name: String,
    val image: String?,
    val description: String,
    val birthDate: String?,
    val creationDate: String?
) : Parcelable

fun PerformerResponse.toModel() = Performer(
    id = id,
    name = name,
    image = image,
    description = description,
    birthDate = birthDate,
    creationDate = creationDate
)
