package com.uniandes.vynilos.presentation.viewModel.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.Comment
import com.uniandes.vynilos.data.model.Tracks
import com.uniandes.vynilos.data.remote.entity.CollectorDTO
import com.uniandes.vynilos.data.repository.AlbumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlbumViewModel(
    val album: Album,
    private val albumRepository: AlbumRepository
) : ViewModel() {

    private val _albumResult = MutableStateFlow<DataState<Album>>(DataState.Idle)
    val albumResult: StateFlow<DataState<Album>> get() = _albumResult
    
    private val _addTrackResult = MutableStateFlow<DataState<Tracks>>(DataState.Idle)
    val addTrackResult: StateFlow<DataState<Tracks>> get() = _addTrackResult

    private val _isAddingComment = MutableStateFlow(false)
    val isAddingComment: StateFlow<Boolean> get() = _isAddingComment

    private val _newComment = MutableStateFlow("")
    val newComment: StateFlow<String> get() = _newComment

    private val _newRating = MutableStateFlow(0)
    val newRating: StateFlow<Int> get() = _newRating

    private val _commentError = MutableStateFlow<String?>(null)
    val commentError: StateFlow<String?> get() = _commentError

    val isCommentButtonEnabled = _newComment.combine(_newRating) { comment, rating ->
        comment.isNotEmpty() && rating > 0
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)



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

    fun addComment() {
        viewModelScope.launch {
            val newComment = Comment(
                id = 0,
                description = _newComment.value,
                rating = _newRating.value,
                collector = CollectorDTO(id = 100)
            )
            val tempResultValue = _albumResult.value
            _albumResult.value = DataState.Loading
            _commentError.value = null
            val result = albumRepository.addComment(album.id!!, newComment)
            if (result is DataState.Success) {
                resetCommentsValue()
                getAlbum()
            } else if (result is DataState.Error) {
                _albumResult.value = tempResultValue
                _commentError.value = result.error.message
            }
        }
    }

    private fun resetCommentsValue() {
        _newComment.value = ""
        _newRating.value = 0
        toggleCommentSection()
    }

    fun toggleCommentSection() {
        _isAddingComment.value = !isAddingComment.value
    }

    fun setComment(value: String) {
        _newComment.value = value
    }

    fun setRating(value: Int) {
        _newRating.value = value
    }
}
