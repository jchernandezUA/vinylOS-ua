package com.uniandes.vynilos.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Diversity2
import androidx.compose.material.icons.filled.Groups
import androidx.compose.ui.graphics.vector.ImageVector
import com.uniandes.vynilos.R

sealed class BottomNavItem(
    @StringRes
    val title: Int,
    val icon: ImageVector,
    val baseRoute: String
) {

    companion object {
        const val ROUTE_ARTIST = "artist"
        const val ROUTE_ALBUMS = "albums"
        const val ROUTE_COLLECTORS = "collectos"

        val BOTTOM_ITEMS = ArrayList(listOf(
            Artists,
            Albums,
            Collectors,
        ))
    }

    data object Artists:  BottomNavItem (
        R.string.artists,
        Icons.Filled.Groups,
        ROUTE_ARTIST)

    data object Albums:  BottomNavItem (
        R.string.albums,
        Icons.Filled.Album,
        ROUTE_ALBUMS)

    data object Collectors:  BottomNavItem (
        R.string.collectors,
        Icons.Filled.Diversity2,
        ROUTE_COLLECTORS)
}

