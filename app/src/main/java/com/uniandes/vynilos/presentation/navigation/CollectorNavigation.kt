package com.uniandes.vynilos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.uniandes.vynilos.presentation.ui.screen.NotMainScreen
import com.uniandes.vynilos.presentation.ui.screen.collector.CollectorDetailScreen
import com.uniandes.vynilos.presentation.viewModel.CollectorDetailViewModel

@Composable
fun CollectorNavigation(
    viewModel: CollectorDetailViewModel,
    onFinish: () -> Unit
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavItem.AlbumDetail.baseRoute
    ) {
        composable(NavItem.AlbumDetail) {
            CollectorDetailScreen(viewModel,NavigationActions{
                onFinish()
            })
        }
        composable(NavItem.NotMain) {
            NotMainScreen()
        }
    }
}