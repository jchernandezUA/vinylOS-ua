package com.uniandes.vynilos.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.uniandes.vynilos.common.NetworkModule
import com.uniandes.vynilos.data.repository.AlbumRepositoryImpl
import com.uniandes.vynilos.presentation.navigation.HomeNavigation
import com.uniandes.vynilos.presentation.viewModel.ListAlbumViewModel
import com.uniandes.vynilos.presentation.viewModel.ListArtistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val listAlbumViewModel = ListAlbumViewModel(
        AlbumRepositoryImpl(NetworkModule.albumServiceAdapter)
    )
    private val listArtistViewModel: ListArtistViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeNavigation(listArtistViewModel, listAlbumViewModel)
        }
    }
}
