@file:Suppress("SpellCheckingInspection")

package com.uniandes.vynilos.presentation.ui.preview

import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.Artist
import com.uniandes.vynilos.data.model.Comment
import com.uniandes.vynilos.data.model.Musician
import com.uniandes.vynilos.data.model.Performer
import com.uniandes.vynilos.data.model.PerformerPrize
import com.uniandes.vynilos.data.model.Tracks
import com.uniandes.vynilos.data.remote.entity.CollectorDTO

object PreviewModel {

    private val tracks = Tracks(
        id = 1,
        name = "El nombre de cancion",
        duration ="10:30"
    )

    private val musician = Musician(
        id = 1,
        name = "The Beatles",
        instrument = "Guitar"
    )

    private val performerPrize = PerformerPrize(
        id = 1,
        premiationDate = 1731108106
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


    private val comment = Comment(
        id = 1,
        rating = 3,
        description = "pues ahí le va",
        collector = CollectorDTO(id = 1)
    )

    private val permorfer = Performer(
        id = 1,
        name = "Rubén Blades",
        description = "Es un mal actor",
        image = "https://upload.wikimedia.org/wikipedia/commons/thumb/b/bb/Ruben_Blades_by_Gage_Skidmore.jpg/800px-Ruben_Blades_by_Gage_Skidmore.jpg",
        birthDate =1245522155
    )


    val album = Album(
        id = 1,
        name = "The Beatles",
        cover = "https://i.pinimg.com/564x/aa/5f/ed/aa5fed7fac61cc8f41d1e79db917a7cd.jpg",
        releaseDate = 1731108106,
        description = "Buscando América es el primer álbum de la banda de Rubén Blades y  los Seis del Solar lanzado en 1984. La producción, bajo el sello Elektra, fusiona diferentes ritmos musicales tales como la salsa, reggae, rock, y el jazz latino. El disco fue grabado en Eurosound Studios en Nueva York entre mayo y agosto de 1983.",
        genre = "Rock",
        recordLabel = "EMI",
        tracks = List(10) {_ -> tracks },
        comments = List(2) {_ -> comment},
        performers = List(10) {_ -> permorfer}
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
}