package com.uniandes.vynilos.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.uniandes.vynilos.presentation.navigation.HomeNavigation
import com.uniandes.vynilos.presentation.viewModel.ListAlbumViewModel
import com.uniandes.vynilos.presentation.viewModel.ListArtistViewModel
import com.uniandes.vynilos.presentation.viewModel.ListCollectorViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val listAlbumViewModel: ListAlbumViewModel by viewModel()
    private val listArtistViewModel: ListArtistViewModel by viewModel()
    private val listCollectorViewModel: ListCollectorViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeNavigation(
                listArtistViewModel = listArtistViewModel,
                listAlbumViewModel = listAlbumViewModel,
                listCollectorViewModel = listCollectorViewModel
            )
        }
    }
}
