package com.uniandes.vynilos.presentation.viewModel.album

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniandes.vynilos.R
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
    val name: StateFlow<String> = _name
    private val _cover = MutableStateFlow("")
    val cover: StateFlow<String> = _cover
    private val _releaseDate = MutableStateFlow("")
    val releaseDate: StateFlow<String> = _releaseDate
    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description
    private val _genre = MutableStateFlow("")
    val genre: StateFlow<String> = _genre
    private val _recordLabel = MutableStateFlow("")
    val recordLabel: StateFlow<String> = _recordLabel


    private val _albumErrorId = MutableStateFlow<Int?>(null)
    val albumErrorId: StateFlow<Int?> = _albumErrorId
    private val _coverErrorId = MutableStateFlow<Int?>(null)
    val coverErrorId: MutableStateFlow<Int?> = _coverErrorId
    private val _descriptionErrorId = MutableStateFlow<Int?>(null)
    val descriptionErrorId: StateFlow<Int?> = _descriptionErrorId
    private val _genreErrorId = MutableStateFlow<Int?>(null)
    val genreErrorId: StateFlow<Int?> = _genreErrorId
    private val _recordLabelErrorId = MutableStateFlow<Int?>(null)
    val recordLabelErrorId: StateFlow<Int?> = _recordLabelErrorId
    private val _releaseDateErrorId = MutableStateFlow<Int?>(null)
    val releaseDateErrorId: StateFlow<Int?> = _releaseDateErrorId


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
        _albumErrorId.value = if (name.isNotEmpty()) null else R.string.required_field
    }

    fun onCoverChange(cover: String) {
        _cover.value = cover
        coverErrorId.value = if (isValidUrl(cover)) null else R.string.invalid_url
    }

    fun onReleaseDateChange(releaseDate: Long) {
        _releaseDate.value = timestampToFormattedString(
            releaseDate,
            "dd-MM-yyyy"
        )
        validateReleaseDate()
    }

    fun onDescriptionChange(description: String) {
        _description.value = description
        _descriptionErrorId.value = if (description.isNotEmpty()) null else R.string.required_field
    }

    fun onGenreChange(genre: String) {
        _genre.value = genre
        _genreErrorId.value = if (genre.isNotEmpty()) null else R.string.required_field
    }

    fun onRecordLabelChange(recordLabel: String) {
        _recordLabel.value = recordLabel
        _recordLabelErrorId.value = if (recordLabel.isNotEmpty()) null else R.string.required_field
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

    fun validateReleaseDate() {
        _releaseDateErrorId.value = if (_releaseDate.value.isNotEmpty()) null else R.string.required_field
    }

    private fun isValidUrl(url: String): Boolean {
       return Patterns.WEB_URL.matcher(url).matches()
    }

}