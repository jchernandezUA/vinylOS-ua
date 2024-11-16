package com.uniandes.vynilos.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.uniandes.vynilos.common.NetworkModule
import com.uniandes.vynilos.common.getSafeParcelableExtra
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.repository.AlbumRepository
import com.uniandes.vynilos.data.repository.AlbumRepositoryImpl
import com.uniandes.vynilos.presentation.navigation.AlbumNavigation
import com.uniandes.vynilos.presentation.viewModel.AlbumViewModel

class AlbumActivity : ComponentActivity() {

    companion object {
        const val ALBUM = "album"
    }

    private lateinit var albumViewModel: AlbumViewModel
    private val albumRepository = AlbumRepositoryImpl(NetworkModule.albumServiceAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ///se recibe el album por medio del intent
        val album = intent.getSafeParcelableExtra<Album>(ALBUM)
        if (album == null) finish()

        albumViewModel = AlbumViewModel(album!!,albumRepository)
        setContent {
            AlbumNavigation(albumViewModel){
                finish()
            }
        }
    }
}