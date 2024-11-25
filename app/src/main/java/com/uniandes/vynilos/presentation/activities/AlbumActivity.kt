package com.uniandes.vynilos.presentation.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.uniandes.vynilos.common.NetworkModule
import com.uniandes.vynilos.common.getSafeParcelableExtra
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.repository.AlbumRepositoryImpl
import com.uniandes.vynilos.presentation.navigation.AlbumNavigation
import com.uniandes.vynilos.presentation.viewModel.album.AlbumViewModel

class AlbumActivity : ComponentActivity() {

    companion object {
        const val ALBUM = "album"
        const val IS_COLLECTOR = "is_collector"

        fun startActivity(
            context: Context,
            album: Album,
            isCollector: Boolean = false
        ) {
            val intent = Intent(context, AlbumActivity::class.java)
            intent.putExtra(ALBUM, album)
            intent.putExtra(IS_COLLECTOR, isCollector)
            context.startActivity(intent)
        }
    }

    private lateinit var albumViewModel: AlbumViewModel
    private val albumRepository = AlbumRepositoryImpl(NetworkModule.albumServiceAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val album = intent.getSafeParcelableExtra<Album>(ALBUM)
        if (album == null) finish()

        albumViewModel = AlbumViewModel(album!!,albumRepository)
        albumViewModel.isCollector = intent.getBooleanExtra(IS_COLLECTOR, false)
        setContent {
            AlbumNavigation(albumViewModel){
                finish()
            }
        }
    }
}