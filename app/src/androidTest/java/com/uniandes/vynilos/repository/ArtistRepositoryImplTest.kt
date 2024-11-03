package com.uniandes.vynilos.repository

import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.DTO
import com.uniandes.vynilos.data.remote.service.ArtistServiceAdapter
import com.uniandes.vynilos.data.repository.ArtistRepositoryImpl
import com.uniandes.vynilos.model.ARTIST_RESPONSE_LIST
import com.uniandes.vynilos.model.NOT_DEFINED_ERROR
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class ArtistRepositoryImplTest {

    private val artistServiceAdapter: ArtistServiceAdapter = mockk()
    private val artistRepository = ArtistRepositoryImpl(artistServiceAdapter)

    @Test
    fun testGetArtistsSuccess() = runBlocking {
        // Given
        val artistResponseList = ARTIST_RESPONSE_LIST
        coEvery { artistServiceAdapter.getArtist() } returns artistResponseList

        // When
        val result = artistRepository.getArtists()

        // Then
        assertTrue(result is DataState.Success)
        assertEquals(artistResponseList.DTO(), (result as DataState.Success).data)
    }

    @Test
    fun testGetArtistsError() = runBlocking {
        // Given
        val errorMessage = NOT_DEFINED_ERROR
        coEvery { artistServiceAdapter.getArtist() } throws Exception(errorMessage)

        // When
        val result = artistRepository.getArtists()

        // Then
        assertTrue(result is DataState.Error)
        assertEquals(errorMessage, (result as DataState.Error).error.message)
    }
}
