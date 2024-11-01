package com.uniandes.vynilos.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.Artist
import com.uniandes.vynilos.data.repository.ArtistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListArtistViewModel(
    private val artistRepository: ArtistRepository
) : ViewModel() {

    //cambiante
    private val _artistResult: MutableStateFlow<DataState<List<Artist>>?> = MutableStateFlow(null)
    //referencia
    val artistResult: StateFlow<DataState<List<Artist>>?>
        get() = _artistResult

    fun getArtists() {
        viewModelScope.launch {
            try {
                viewModelScope.launch {
                    _artistResult.value = DataState.Loading
                    _artistResult.value = artistRepository.getArtists() //success or error
                }
            } catch (e: Exception) {
                _artistResult.value = DataState.Error(e)
            }
        }
    }
}