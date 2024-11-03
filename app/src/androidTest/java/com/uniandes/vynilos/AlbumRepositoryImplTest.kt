package com.uniandes.vynilos

import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.DTO
import com.uniandes.vynilos.data.remote.service.AlbumServiceAdapter
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
}
