package com.uniandes.vynilos.model

import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.Artist
import com.uniandes.vynilos.data.remote.entity.AlbumResponse
import com.uniandes.vynilos.data.remote.entity.ArtistResponse

val ARTIST_LIST = List(10) { index ->
    createArtist(
        id = index + 1,
        name = "Artist Name ${index + 1}"
    )
}

val ARTIST_RESPONSE_LIST = List(10) { index ->
    ArtistResponse(
        id = index + 1,
        name = "Artist Name ${index + 1}",
        image = "https://www.example.com/artist${index + 1}.png",
        description = "Description of artist ${index + 1}",
        creationDate = "2024-11-08T15:35:22.123Z",
        albums = emptyList(),
        musicians = emptyList(),
        performerPrizes = emptyList()
    )
}

val ALBUM_LIST = List(10) { index ->
    createAlbum(
        id = index + 1,
        name = "Album Name ${index + 1}"
    )
}

val ALBUM_RESPONSE_LIST = List(10) { index ->
    AlbumResponse(
        id = index + 1,
        name = "Album Name ${index + 1}",
        cover = "https://www.example.com/album${index + 1}.png",
        description = "Description of album ${index + 1}",
        releaseDate = "2024-11-08T15:35:22.123Z",
        genre = "",
        recordLabel = "",
        tracks = emptyList(),
        performers = emptyList(),
        comments = emptyList(),

    )
}

const val DEFAULT_ERROR = "Something went wrong"
const val NOT_DEFINED_ERROR = "Not defined error"

fun createArtist(
    id: Int = 1,
    name: String = "Artist Name",
    image: String = "https://www.example.com/artist.png",
    description: String = "Description of an artist"
): Artist {
    return Artist(
        id = id,
        name = name,
        image = image,
        description = description,
        albums = emptyList(),
        musicians = emptyList(),
        performerPrizes = emptyList(),
        creationDate = 0
    )
}

fun createAlbum(
    id: Int = 1,
    name: String = "Album Name",
    cover: String = "https://www.example.com/album.png",
    description: String = "Description of an album"
): Album {
    return Album(
        id = id,
        name = name,
        cover = cover,
        description = description,
        releaseDate = 0,
        genre = "",
        recordLabel = "",
        tracks = emptyList(),
        performers = emptyList(),
        comments = emptyList(),
    )
}
