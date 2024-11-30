package com.uniandes.vynilos.data.remote.entity

data class CommentRequest(
    val description: String,
    val rating: Int,
    val collector: CollectorDTO
)