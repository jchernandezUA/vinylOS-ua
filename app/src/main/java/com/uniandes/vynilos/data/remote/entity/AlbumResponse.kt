package com.uniandes.vynilos.data.remote.entity

data class AlbumResponse(
    val id: Int,
    val name: String,
    val cover: String,
    val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String,
    val tracks: List<TracksResponse>?,
    val performers: List<PerformerResponse>?,
    val comments: List<CommentResponse>?
)

data class CommentResponse(
    val id: Int,
    val description: String,
    val rating: Int,
    val collectorId: Int
)

data class TracksResponse(
    val id: Int,
    val name: String,
    val duration: String
)
