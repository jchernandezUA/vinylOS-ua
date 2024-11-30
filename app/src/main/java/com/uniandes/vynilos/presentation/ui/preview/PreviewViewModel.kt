package com.uniandes.vynilos.presentation.ui.preview

import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.Artist
import com.uniandes.vynilos.data.model.Comment
import com.uniandes.vynilos.data.model.Tracks
import com.uniandes.vynilos.data.repository.AlbumRepository
import com.uniandes.vynilos.data.repository.ArtistRepository
import com.uniandes.vynilos.presentation.viewModel.ArtistViewModel
import com.uniandes.vynilos.presentation.viewModel.ListArtistViewModel
import com.uniandes.vynilos.presentation.viewModel.album.AddAlbumViewModel
import com.uniandes.vynilos.presentation.viewModel.album.AlbumViewModel

object PreviewViewModel {

    private fun getArtistRepository(
        artist: Artist? = null,
        artistList: List<Artist>? = null
    ) = object : ArtistRepository {
        override suspend fun getArtists(): DataState<List<Artist>> {
            return artistList?.let {
                DataState.Success(it)
            } ?: DataState.Error(Exception("Preview Error"))
        }

        override suspend fun getArtist(id: Int): DataState<Artist> {
            return artist?.let {
                DataState.Success(it)
            } ?: DataState.Error(Exception("Preview Error"))
        }
    }

    fun getArtistViewModel(artist: Artist? = PreviewModel.artist) = ArtistViewModel(
        artist = artist ?: PreviewModel.artist,
        artistRepository = getArtistRepository(artist = artist)
    )

    fun getListArtistViewModel(artistList: List<Artist>? = List(10) { PreviewModel.artist }) =
        ListArtistViewModel(getArtistRepository(artistList = artistList))

    private fun getAlbumRepository(
        album: Album? = null,
        albumList: List<Album>? = null
    ) = object : AlbumRepository {
        override suspend fun getAlbums(): DataState<List<Album>> {
            return albumList?.let {
                DataState.Success(it)
            } ?: DataState.Error(Exception("Preview Error"))
        }

        override suspend fun getAlbum(id: Int): DataState<Album> {
            return if (album != null) {
                DataState.Success(album)
            } else {
                DataState.Error(Exception("Preview Error"))
            }
            return album?.let {
                DataState.Success(it)
            } ?: DataState.Error(Exception("Preview Error"))
        }

        override suspend fun addComment(albumId: Int, comment: Comment): DataState<Unit> {
            return DataState.Success(Unit)
        }

        override suspend fun addAlbum(album: Album): DataState<Album> {
            return if (album != null) {
                DataState.Success(album)
            } else {
                DataState.Error(Exception("Preview Error"))
            }
        }

        override suspend fun addTrackToAlbum(albumId: Int, track: Tracks): DataState<Tracks> {
            // Provide a dummy implementation for preview purposes
            return DataState.Success(track)
        }
    }



    fun getAlbumViewModel(album: Album? = PreviewModel.album) = AlbumViewModel(
        album = album ?: PreviewModel.album,
        albumRepository = getAlbumRepository(
            album = album,
        )
    )

    fun getAddAlbumViewModel() = AddAlbumViewModel(
        albumRepository = getAlbumRepository()
    )

}
