package com.uniandes.vynilos.presentation.navigation

import android.content.IntentSender.OnFinished
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.presentation.ui.screen.NotMainScreen
import com.uniandes.vynilos.presentation.ui.screen.album.AlbumDetalScreen
import com.uniandes.vynilos.presentation.viewModel.AlbumViewModel

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
            AlbumDetalScreen(viewModel,NavigationActions{
                onFinish()
            })
        }

        composable(NavItem.NotMain) {
            NotMainScreen()
        }
    }
}