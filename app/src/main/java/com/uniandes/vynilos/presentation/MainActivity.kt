package com.uniandes.vynilos.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.uniandes.vynilos.common.NetworkModule
import com.uniandes.vynilos.data.repository.AlbumRepositoryImpl
import com.uniandes.vynilos.data.repository.ArtistRepositoryImpl
import com.uniandes.vynilos.presentation.navigation.ActionType
import com.uniandes.vynilos.presentation.navigation.HomeNavigation
import com.uniandes.vynilos.presentation.navigation.NavItem
import com.uniandes.vynilos.presentation.navigation.NavigationActions
import com.uniandes.vynilos.presentation.navigation.composable
import com.uniandes.vynilos.presentation.ui.screen.MainScreen
import com.uniandes.vynilos.presentation.ui.screen.NotMainScreen
import com.uniandes.vynilos.presentation.viewModel.ListAlbumViewModel
import com.uniandes.vynilos.presentation.viewModel.ListArtistViewModel

class MainActivity : ComponentActivity() {

    private val listAlbumViewModel = ListAlbumViewModel(
        AlbumRepositoryImpl(NetworkModule.albumServiceAdapter)
    )
    private val listArtistViewModel = ListArtistViewModel(
        ArtistRepositoryImpl(NetworkModule.artistServiceAdapter)
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeNavigation(this)

        }
    }
}
