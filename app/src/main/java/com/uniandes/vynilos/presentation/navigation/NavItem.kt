package com.uniandes.vynilos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

sealed class NavItem (val baseRoute: String) {
    object  Main: NavItem("main")
    object  NotMain: NavItem("not_main")
    object  AlbumDetail: NavItem("album_detail")
}

fun NavGraphBuilder.composable(
    navItem: NavItem,
    content: @Composable (NavBackStackEntry) -> Unit) {
    composable(
        route= navItem.baseRoute
    ) {
        content(it)
    }
}

fun NavGraphBuilder.composable(
    navItem: BottomNavItem,
    content: @Composable (NavBackStackEntry) -> Unit) {
    composable(
        route= navItem.baseRoute
    ) {
        content(it)
    }
}
