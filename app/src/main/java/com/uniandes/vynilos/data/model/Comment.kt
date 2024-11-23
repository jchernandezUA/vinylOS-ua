package com.uniandes.vynilos.data.model

import android.os.Parcelable
import com.uniandes.vynilos.data.remote.entity.CollectorDTO
import com.uniandes.vynilos.data.remote.entity.CommentRequest
import com.uniandes.vynilos.data.remote.entity.CommentResponse
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Comment(
    val id: Int,
    val description: String,
    val rating: Int,
    val collector: @RawValue CollectorDTO
) : Parcelable

fun CommentResponse.DTO() = Comment(
    id = id,
    description = description,
    rating = rating,
    collector = CollectorDTO(id = collectorId)
)

fun List<CommentResponse>.DTO() = map { it.DTO() }

fun Comment.toDomain() = CommentRequest(
    description = description,
    rating = rating,
    collector = collector
)
