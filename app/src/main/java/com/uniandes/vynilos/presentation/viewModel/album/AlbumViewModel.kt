package com.uniandes.vynilos.presentation.viewModel.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.Tracks
import com.uniandes.vynilos.data.repository.AlbumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlbumViewModel(
    val album: Album,
    private val albumRepository: AlbumRepository
) : ViewModel() {

    private val _albumResult = MutableStateFlow<DataState<Album>>(DataState.Idle)
    val albumResult: StateFlow<DataState<Album>> get() = _albumResult

    private val _addTrackResult = MutableStateFlow<DataState<Tracks>>(DataState.Idle)
    val addTrackResult: StateFlow<DataState<Tracks>> get() = _addTrackResult

    var isCollector: Boolean = false

    fun getAlbum() {
        viewModelScope.launch {
            _albumResult.value = DataState.Loading
            _albumResult.value = albumRepository.getAlbum(album.id!!)
        }
    }

    fun addTrackToAlbum(trackName: String, trackDuration: String) {
        viewModelScope.launch {
            _addTrackResult.value = DataState.Loading
            val track = Tracks(
                name = trackName,
                duration = trackDuration
            )
            val result = albumRepository.addTrackToAlbum(album.id!!, track)
            _addTrackResult.value = result

            if (result is DataState.Success) {
                getAlbum()
            }
        }
    }

    fun resetAddTrackResult() {
        _addTrackResult.value = DataState.Idle
    }
}
