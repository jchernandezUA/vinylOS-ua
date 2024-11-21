package com.uniandes.vynilos.presentation.viewModel.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.repository.AlbumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlbumViewModel(
    val album : Album,
    private val albumRepository: AlbumRepository
) : ViewModel() {

    private val _albumResult = MutableStateFlow<DataState<Album>>(DataState.Idle)
    val albumResult: StateFlow<DataState<Album>?> = _albumResult

    fun getAlbum(){
        viewModelScope.launch {
            _albumResult.value = DataState.Loading
            _albumResult.value = albumRepository.getAlbum(album.id!!)
        }
    }
}