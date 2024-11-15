package com.uniandes.vynilos.presentation.ui.preview

import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.Artist
import com.uniandes.vynilos.data.model.Comment
import com.uniandes.vynilos.data.model.Musician
import com.uniandes.vynilos.data.model.Performer
import com.uniandes.vynilos.data.model.PerformerPrize
import com.uniandes.vynilos.data.model.Tracks

object PreviewModel {

    val album = Album(
        id = 1,
        name = "The Beatles",
        cover = "https:/immage.com",
        releaseDate = 1731108106,
        description = "dasdsadsadasda",
        genre = "Rock",
        recordLabel = "EMI",
        tracks = List(2) {trakcs},
        comments = List(2) {comment},
        performers = List(2) {permorfer}
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

    val trakcs = Tracks(
        id = 1,
        name = "El cantor",
        duration ="10:30"
    )

    val comment = Comment(
        id = 1,
        rating = 3,
        description = "pues ahí le va"
    )

    val permorfer = Performer(
        id = 1,
        name = "Rubén Blades",
        description = "Es un mal actor",
        image = "https://upload.wikimedia.org/wikipedia/commons/thumb/b/bb/Ruben_Blades_by_Gage_Skidmore.jpg/800px-Ruben_Blades_by_Gage_Skidmore.jpg",
        birthDate =1245522155
    )
}