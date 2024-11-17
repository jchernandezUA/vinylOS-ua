package com.uniandes.vynilos.data.remote.entity

data class CollectorResponse(
    val id: Int,
    val name: String,
    val telephone: String,
    val email: String,
    val comments: List<CommentResponse> = emptyList(),
    val favoritePerformers: List<PerformerResponse> = emptyList(),
    val collectorAlbums: List<CollectorAlbumResponse> = emptyList()
)

data class CollectorAlbumResponse(
    val id: Int,
    val price: Double,
    val status: String
)
