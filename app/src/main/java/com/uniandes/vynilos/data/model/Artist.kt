package com.uniandes.vynilos.data.model

import com.uniandes.vynilos.common.convertDateToTimestamp
import com.uniandes.vynilos.data.remote.entity.ArtistResponse
import com.uniandes.vynilos.data.remote.entity.MusicianResponse
import com.uniandes.vynilos.data.remote.entity.PerformerPrizeResponse


data class Artist(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val creationDate: Long,
    val albums: List<Album>,
    val musicians: List<Musician>,
    val performerPrizes: List<PerformerPrize>
)

fun ArtistResponse.DTO() = Artist(
    id = id,
    name = name,
    image = image,
    description = description,
    creationDate = creationDate.convertDateToTimestamp(),
    albums = albums.DTO(),
    musicians = musicians.DTO(),
    performerPrizes = performerPrizes.DTO()
)

fun List<ArtistResponse>.DTO() = map { it.DTO() }

