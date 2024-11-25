package com.uniandes.vynilos.repository

import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.Comment
import com.uniandes.vynilos.data.model.DTO
import com.uniandes.vynilos.data.model.Performer
import com.uniandes.vynilos.data.model.Tracks
import com.uniandes.vynilos.data.remote.entity.AlbumResponse
import com.uniandes.vynilos.data.remote.entity.CollectorDTO
import com.uniandes.vynilos.data.remote.entity.CommentRequest
import com.uniandes.vynilos.data.remote.service.AlbumServiceAdapter
import com.uniandes.vynilos.data.repository.AlbumRepository
import com.uniandes.vynilos.data.repository.AlbumRepositoryImpl
import com.uniandes.vynilos.model.ALBUM_RESPONSE_LIST
import com.uniandes.vynilos.model.NOT_DEFINED_ERROR
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AlbumRepositoryImplTest {

    private val albumServiceAdapter: AlbumServiceAdapter = mockk()
    private val albumRepository = AlbumRepositoryImpl(albumServiceAdapter)

    @Test
    fun testGetAlbumsSuccess() = runBlocking {
        // Given
        val albumResponseList = ALBUM_RESPONSE_LIST
        coEvery { albumServiceAdapter.getAlbums() } returns albumResponseList

        // When
        val result = albumRepository.getAlbums()

        // Then
        assertTrue(result is DataState.Success)
        assertEquals(albumResponseList.DTO(), (result as DataState.Success).data)
    }

    @Test
    fun testGetAlbumsError() = runBlocking {
        // Given
        val errorMessage = NOT_DEFINED_ERROR
        coEvery { albumServiceAdapter.getAlbums() } throws Exception(errorMessage)

        // When
        val result = albumRepository.getAlbums()

        // Then
        assertTrue(result is DataState.Error)
        assertEquals(errorMessage, (result as DataState.Error).error.message)
    }

    @Test
    fun testGetAlbumSuccess() = runBlocking {
        // Given
        val albumId = 1
        val albumResponse: AlbumResponse = ALBUM_RESPONSE_LIST[albumId - 1]
        val expectedAlbumDTO = albumResponse.DTO()
        coEvery { albumServiceAdapter.getAlbumById(albumId) } returns albumResponse

        // When
        val result = albumRepository.getAlbum(albumId)

        // Then
        assertTrue(result is DataState.Success)
        assertEquals(expectedAlbumDTO, (result as DataState.Success).data)
    }

    @Test
    fun testGetAlbumError() = runBlocking {
        // Given
        val albumId = 1
        val errorMessage = NOT_DEFINED_ERROR
        coEvery { albumServiceAdapter.getAlbumById(albumId) } throws Exception(errorMessage)

        // When
        val result = albumRepository.getAlbum(albumId)

        // Then
        assertTrue(result is DataState.Error)
        assertEquals(errorMessage, (result as DataState.Error).error.message)
    }

    @Test
    fun testAddCommentSuccess() = runBlocking {
        // Given
        val albumId = 1
        val comment = CommentRequest(
            description = "Amazing album!",
            rating = 5,
            collector = CollectorDTO(id = 123)
        )
        val comments = Comment(
            id = 1,
            description = "Amazing album!",
            rating = 5,
            collector = CollectorDTO(id = 123)
        )
        val albumResponse: AlbumResponse = ALBUM_RESPONSE_LIST[albumId - 1]
        coEvery { albumServiceAdapter.addComment(albumId, comment) } returns albumResponse

        // When
        val result = albumRepository.addComment(albumId, comments)

        // Then
        assertTrue(result is DataState.Success)
        assertEquals(Unit, (result as DataState.Success).data)
    }

    @Test
    fun testAddCommentError() = runBlocking {
        // Given
        val albumId = 1
        val commentRequest = CommentRequest(
            description = "Amazing album!",
            rating = 5,
            collector = CollectorDTO(id = 123)
        )
        val comments = Comment(
            id = 1,
            description = "Amazing album!",
            rating = 5,
            collector = CollectorDTO(id = 123)
        )
        val errorMessage = "Error adding comment"
        coEvery { albumServiceAdapter.addComment(albumId, commentRequest) } throws Exception(errorMessage)

        // When
        val result = albumRepository.addComment(albumId, comments)

        // Then
        assertTrue(result is DataState.Error)
    }



}
