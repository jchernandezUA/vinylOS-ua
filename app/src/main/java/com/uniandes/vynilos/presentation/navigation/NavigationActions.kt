package com.uniandes.vynilos.presentation.navigation

import com.uniandes.vynilos.data.model.Album

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

