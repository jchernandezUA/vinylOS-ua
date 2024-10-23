package com.uniandes.vynilos.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.uniandes.vynilos.presentation.navigation.ActionType
import com.uniandes.vynilos.presentation.navigation.NavItem
import com.uniandes.vynilos.presentation.navigation.NavigationActions
import com.uniandes.vynilos.presentation.navigation.composable
import com.uniandes.vynilos.presentation.ui.screen.MainScreen
import com.uniandes.vynilos.presentation.ui.screen.NotMainScreen
import com.uniandes.vynilos.presentation.viewModel.AlbumViewModel

class MainActivity : ComponentActivity() {
    private val albumViewModel = AlbumViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = NavItem.Main.baseRoute
            ) {
                composable(NavItem.Main) {
                    MainScreen(albumViewModel, navigationActions = NavigationActions {
                        if (it == ActionType.CLICK_NOT_MAIN) {
                            navController.navigate(NavItem.NotMain.baseRoute)
                        }
                    })
                }

                composable(NavItem.NotMain) {
                    NotMainScreen()
                }
            }
        }
    }
}
