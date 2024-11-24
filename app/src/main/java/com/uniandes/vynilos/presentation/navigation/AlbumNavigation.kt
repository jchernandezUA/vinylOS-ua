package com.uniandes.vynilos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.uniandes.vynilos.presentation.ui.screen.NotMainScreen
import com.uniandes.vynilos.presentation.ui.screen.album.AlbumDetailScreen
import com.uniandes.vynilos.presentation.viewModel.album.AlbumViewModel

@Composable
fun AlbumNavigation(
    viewModel: AlbumViewModel,
    onFinish: () -> Unit
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavItem.AlbumDetail.baseRoute
    ) {
        composable(NavItem.AlbumDetail) {
            AlbumDetailScreen(
                viewModel = viewModel,
                isCollector = true,
                NavigationActions {
                    onFinish()
                }
            )
        }

        composable(NavItem.NotMain) {
            NotMainScreen()
        }
    }
}