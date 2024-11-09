package com.uniandes.vynilos.presentation.ui.preview

import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Artist
import com.uniandes.vynilos.data.repository.ArtistRepository
import com.uniandes.vynilos.presentation.viewModel.ArtistViewModel
import com.uniandes.vynilos.presentation.viewModel.ListArtistViewModel

object PreviewViewModel {

    private fun getArtistRepository(
        artist: Artist? = null,
        artistList: List<Artist>? = null
    ) = object : ArtistRepository {
        override suspend fun getArtists(): DataState<List<Artist>> {
            return if (artistList != null) {
                DataState.Success(artistList)
            } else {
                DataState.Error(Exception("Preview Error"))
            }
        }

        override suspend fun getArtist(id: Int): DataState<Artist> {
            return if(artist != null) {
                DataState.Success(artist)
            } else {
                DataState.Error(Exception("Preview Error"))
            }
        }
    }

    fun getArtistViewModel(artist: Artist? = PreviewModel.artist) = ArtistViewModel(
        artist = artist?: PreviewModel.artist,
        artistRepository = getArtistRepository(
            artist = artist,
        )
    )

    fun getListArtistViewModel(artistList: List<Artist>? = List(10){PreviewModel.artist})
    = ListArtistViewModel(getArtistRepository(artistList = artistList))
}