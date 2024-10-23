package com.uniandes.vynilos.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.repository.AlbumsRepositoryImpl
import com.uniandes.vynilos.common.NetworkModule
import com.uniandes.vynilos.data.model.Album
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlbumViewModel: ViewModel() {

    private val exampleRepository = AlbumsRepositoryImpl(NetworkModule.userService)
    //cambiante
    private val _albumsResult: MutableStateFlow<DataState<List<Album>>?> = MutableStateFlow(null)
    //referencia
    val albumsResult: StateFlow<DataState<List<Album>>?>
        get() = _albumsResult


    fun getAlbums() {
        viewModelScope.launch {
            _albumsResult.value = DataState()  //loading
            _albumsResult.value = exampleRepository.getAlbums() //success or error
        }
    }
}