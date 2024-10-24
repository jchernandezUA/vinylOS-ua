package com.uniandes.vynilos.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.uniandes.vynilos.R
import java.util.ArrayList

sealed class BottomNavItem(
    @StringRes
    val title: Int,
    @DrawableRes
    val icon: Int,
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
        R.drawable.ic_home,
        ROUTE_ARTIST)

    data object Albums:  BottomNavItem (
        R.string.albums,
        R.drawable.ic_home,
        ROUTE_ALBUMS)

    data object Collectors:  BottomNavItem (
        R.string.collectors,
        R.drawable.ic_home,
        ROUTE_COLLECTORS)
}

