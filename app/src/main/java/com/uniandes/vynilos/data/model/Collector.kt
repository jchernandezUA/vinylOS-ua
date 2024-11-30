package com.uniandes.vynilos.data.model

import android.os.Parcelable
import com.uniandes.vynilos.data.remote.entity.CollectorResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class Collector(
    val id: Int,
    val name: String,
    val telephone: String,
    val email: String,
    val comments: List<Comment> = emptyList(),
    val favoritePerformers: List<Performer> = emptyList()
) : Parcelable

fun CollectorResponse.toModel() = Collector(
    id = id,
    name = name,
    telephone = telephone,
    email = email,
    comments = comments.map { it.DTO() },
    favoritePerformers = favoritePerformers.map { it.DTO() }
)

fun List<CollectorResponse>.toModelList() = map { it.toModel() }
