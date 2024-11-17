package com.uniandes.vynilos.presentation.navigation

import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.Artist

class NavigationActions (
    val onAction: (type: ActionType) -> Unit = {}
)


sealed class ActionType {
    data object ClickNotMain : ActionType()
    data object OnBack: ActionType()
}


sealed class AlbumActions : ActionType() {
    class OnClickAlbum(val album: Album) : AlbumActions()
}

sealed class ArtistActions : ActionType() {
    class OnClickArtist(val artist: Artist) : ArtistActions()
}

