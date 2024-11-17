package com.uniandes.vynilos.data.model

import android.os.Parcelable
import com.uniandes.vynilos.data.remote.entity.CommentResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
    val id: Int,
    val description: String
) : Parcelable

fun CommentResponse.toModel() = Comment(
    id = id,
    description = description
)
