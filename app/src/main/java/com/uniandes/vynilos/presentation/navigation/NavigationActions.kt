package com.uniandes.vynilos.presentation.navigation

import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.Artist
import com.uniandes.vynilos.data.model.Collector

class NavigationActions (
    val onAction: (type: ActionType) -> Unit = {}
)


sealed class ActionType {
    data object OnBack: ActionType()
}


sealed class AlbumActions : ActionType() {
    class OnClickAlbum(val album: Album) : AlbumActions()
    data object OnClickAddAlbum : AlbumActions()
    data object OnAlbumAdded: AlbumActions()
}

sealed class ArtistActions : ActionType() {
    class OnClickArtist(val artist: Artist) : ArtistActions()
}

sealed class CollectorActions : ActionType() {
    class OnClickCollector(val collector: Collector) : CollectorActions()
}

