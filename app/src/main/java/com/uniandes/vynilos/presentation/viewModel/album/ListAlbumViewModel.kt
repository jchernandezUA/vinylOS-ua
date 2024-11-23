package com.uniandes.vynilos.presentation.viewModel.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.Artist
import com.uniandes.vynilos.data.model.Collector
import com.uniandes.vynilos.data.model.Comment
import com.uniandes.vynilos.data.remote.entity.CollectorDTO
import com.uniandes.vynilos.data.repository.AlbumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListAlbumViewModel(
    private val albumRepository: AlbumRepository
): ViewModel() {

    private val _albumsResult: MutableStateFlow<DataState<List<Album>>?> = MutableStateFlow(null)
    val albumsResult: StateFlow<DataState<List<Album>>?>
        get() = _albumsResult


    fun getAlbums() {
        viewModelScope.launch {
            _albumsResult.value = DataState.Loading
            _albumsResult.value = albumRepository.getAlbums() //success or error
        }
    }
    fun addComment(albumId: Int, description: String) {
        viewModelScope.launch {
            val newComment = Comment(
                id = 0,
                description = description,
                rating = 5,
                collector = CollectorDTO(id = 100)
            )
            _albumResult.value = DataState.Loading
            albumRepository.addComment(albumId, newComment)
        }
    }
}