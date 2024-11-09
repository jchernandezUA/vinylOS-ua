package com.uniandes.vynilos.data.remote.entity

data class ArtistResponse(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val creationDate: String,
    val albums: List<AlbumResponse>,
    val musicians: List<MusicianResponse>,
    val performerPrizes: List<PerformerPrizeResponse>
)

data class MusicianResponse(
    val id: Int,
    val name: String,
    val instrument: String
)

data class PerformerPrizeResponse(
    val id: Int,
    val premiationDate: String
)