package com.uniandes.vynilos.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Artist
import com.uniandes.vynilos.data.repository.ArtistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArtistViewModel(
    val artist: Artist,
    private val artistRepository: ArtistRepository
) : ViewModel() {

    private val _artistResult = MutableStateFlow<DataState<Artist>>(DataState.Idle)
    val artistResult: StateFlow<DataState<Artist>?> = _artistResult


    fun getArtist() {
        viewModelScope.launch {
            _artistResult.value = DataState.Loading
            _artistResult.value = artistRepository.getArtist(artist.id)
        }
    }

}