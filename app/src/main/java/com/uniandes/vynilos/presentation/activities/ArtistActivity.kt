package com.uniandes.vynilos.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.uniandes.vynilos.common.NetworkModule
import com.uniandes.vynilos.common.getSafeParcelableExtra
import com.uniandes.vynilos.data.model.Artist
import com.uniandes.vynilos.data.repository.ArtistRepository
import com.uniandes.vynilos.data.repository.ArtistRepositoryImpl
import com.uniandes.vynilos.presentation.navigation.ArtistNavigation
import com.uniandes.vynilos.presentation.viewModel.ArtistViewModel

class ArtistActivity : ComponentActivity() {

    companion object {
        const val ARTIST = "artist"
    }

    private lateinit var artistViewModel: ArtistViewModel
    private val artistRepository: ArtistRepository = ArtistRepositoryImpl(NetworkModule.artistServiceAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val artist = intent.getSafeParcelableExtra<Artist>(ARTIST)
        if (artist == null) finish()

        artistViewModel = ArtistViewModel(artist!!, artistRepository)
        setContent {
            ArtistNavigation(artistViewModel) {
                finish()
            }
        }
    }
}
