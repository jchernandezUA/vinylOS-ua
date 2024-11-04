package com.uniandes.vynilos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.presentation.ui.screen.NotMainScreen

@Composable
fun AlbumNavigation(
    /*solo ejemplo, usar viewModel*/
    album: Album
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavItem.AlbumDetail.baseRoute
    ) {
        composable(NavItem.AlbumDetail) {
            NotMainScreen(album.name)
        }

        composable(NavItem.NotMain) {
            NotMainScreen()
        }
    }
}