package com.uniandes.vynilos.presentation.ui.preview

import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.Artist
import com.uniandes.vynilos.data.model.Musician
import com.uniandes.vynilos.data.model.PerformerPrize

object PreviewModel {

    val album = Album(
        id = 1,
        name = "The Beatles",
        cover = "https:/immage.com",
        releaseDate = 1731108106,
        description = "dasdsadsadasda",
        genre = "Rock",
        recordLabel = "EMI"
    )

    val musician = Musician(
        id = 1,
        name = "The Beatles",
        instrument = "Guitar"
    )

    val performerPrize = PerformerPrize(
        id = 1,
        premiationDate = 1731108106
    )

    val artist = Artist(
        id = 1,
        name = "The Beatles",
        description = "description",
        image = "https:/immage.com",
        albums = List(15) { _ -> album },
        musicians = List(15) { _ -> musician},
        performerPrizes = List(15) {performerPrize},
        creationDate = 1731108106
    )

    val artistEmptyValues = Artist(
        id = 1,
        name = "The Beatles",
        description = "description",
        image = "https:/immage.com",
        albums = emptyList(),
        musicians = emptyList(),
        performerPrizes = emptyList(),
        creationDate = 1731108106
    )
}