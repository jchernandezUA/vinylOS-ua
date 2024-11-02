package com.uniandes.vynilos.model

import com.uniandes.vynilos.data.model.Artist


val ARTIST_LIST = List(10) { index ->
    createArtist(
        id = index + 1,
        name = "Artist Name ${index + 1}"
    )
}
const val DEFAULT_ERROR = "Something went wrong"

fun createArtist(
    id: Int = 1,
    name: String = "Artist Name",
    image: String = "https://www.google.com/image.png:",
    description: String = "Description of an artist"
): Artist {
    return Artist(
        id = id,
        name = name,
        image = image,
        description = description
    )
}