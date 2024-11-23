package com.uniandes.vynilos.data.remote.entity

import com.uniandes.vynilos.data.model.Collector

data class CommentRequest(
    val description: String,
    val rating: Int,
    val collector: CollectorDTO
)