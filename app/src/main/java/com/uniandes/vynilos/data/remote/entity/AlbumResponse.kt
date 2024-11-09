package com.uniandes.vynilos.data.remote.entity

data class AlbumResponse(
    val id: Int,
    val name: String,
    val cover: String,
    val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String
)