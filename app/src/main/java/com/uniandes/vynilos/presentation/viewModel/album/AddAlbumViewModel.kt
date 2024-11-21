package com.uniandes.vynilos.presentation.viewModel.album

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.common.convertDateToTimestamp
import com.uniandes.vynilos.common.timestampToFormattedString
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.repository.AlbumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AddAlbumViewModel(
    private val albumRepository: AlbumRepository
): ViewModel() {

    private val _albumResult = MutableStateFlow<DataState<Album>>(DataState.Idle)
    val albumResult: MutableStateFlow<DataState<Album>> = _albumResult

    private val _name = MutableStateFlow("")
    val name: MutableStateFlow<String> = _name
    private val _cover = MutableStateFlow("")
    val cover: MutableStateFlow<String> = _cover
    private val _releaseDate = MutableStateFlow("")
    val releaseDate: MutableStateFlow<String> = _releaseDate
    private val _description = MutableStateFlow("")
    val description: MutableStateFlow<String> = _description
    private val _genre = MutableStateFlow("")
    val genre: MutableStateFlow<String> = _genre
    private val _recordLabel = MutableStateFlow("")
    val recordLabel: MutableStateFlow<String> = _recordLabel

    val isValidForm: StateFlow<Boolean> = combine(
        _name,
        _cover,
        _releaseDate,
        _description,
        _genre
    ){ name, cover, releaseDate, description, genre ->
        name.isNotEmpty() &&
                cover.isNotEmpty() &&
                isValidUrl(cover) &&
                releaseDate.isNotEmpty() &&
                description.isNotEmpty() &&
                genre.isNotEmpty()
    }.combine(_recordLabel){ combination, recordLabel ->
        combination && recordLabel.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)


    fun onNameChange(name: String) {
        _name.value = name
    }

    fun onCoverChange(cover: String) {
        _cover.value = cover
    }

    fun onReleaseDateChange(releaseDate: Long) {
        _releaseDate.value = timestampToFormattedString(
            releaseDate,
            "dd-MM-yyyy"
        )
    }

    fun onDescriptionChange(description: String) {
        _description.value = description
    }

    fun onGenreChange(genre: String) {
        _genre.value = genre
    }

    fun onRecordLabelChange(recordLabel: String) {
        _recordLabel.value = recordLabel
    }

    fun addAlbum() {
        val album = Album(
            name = _name.value,
            cover = _cover.value,
            releaseDate = _releaseDate.value.convertDateToTimestamp(
                "dd-MM-yyyy"
            ),
            description = _description.value,
            recordLabel = _recordLabel.value,
            genre = _genre.value
        )
        viewModelScope.launch {
            _albumResult.value = DataState.Loading
            _albumResult.value = albumRepository.addAlbum(album)
        }
    }

    private fun isValidUrl(url: String): Boolean {
       return Patterns.WEB_URL.matcher(url).matches()
    }

}