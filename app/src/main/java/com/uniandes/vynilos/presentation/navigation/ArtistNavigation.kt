package com.uniandes.vynilos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.uniandes.vynilos.data.model.Artist
import com.uniandes.vynilos.presentation.ui.screen.NotMainScreen
import com.uniandes.vynilos.presentation.ui.screen.artist.ArtistDetailScreen
import com.uniandes.vynilos.presentation.viewModel.ArtistViewModel

@Composable
fun ArtistNavigation(
    viewModel: ArtistViewModel,
    onFinish: () -> Unit
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavItem.ArtistDetail.baseRoute
    ) {
        composable(NavItem.ArtistDetail) {
            ArtistDetailScreen(viewModel, NavigationActions {
                onFinish()
            })
        }

        composable(NavItem.NotMain) {
            NotMainScreen()
        }
    }
}
