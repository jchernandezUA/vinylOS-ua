package com.uniandes.vynilos.viewModel

import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Comment
import com.uniandes.vynilos.data.remote.entity.CollectorDTO
import com.uniandes.vynilos.data.repository.AlbumRepository
import com.uniandes.vynilos.model.ALBUM_LIST
import com.uniandes.vynilos.model.DEFAULT_ERROR
import com.uniandes.vynilos.presentation.viewModel.album.AlbumViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumViewModelTest {

    private val albumRepository: AlbumRepository = mockk()
    private lateinit var viewModel: AlbumViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val testAlbum = ALBUM_LIST.first()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AlbumViewModel(testAlbum, albumRepository)
    }

    @Test
    fun testInitialState() = runTest {
        val initialState = viewModel.albumResult.first()
        assertEquals(DataState.Idle, initialState)
    }

    @Test
    fun testGetAlbumSuccess() = runTest {
        coEvery { albumRepository.getAlbum(testAlbum.id!!) } returns DataState.Success(testAlbum)

        viewModel.getAlbum()
        runCurrent()

        val result = viewModel.albumResult.first()
        assertTrue(result is DataState.Success)
        assertEquals(testAlbum, (result as DataState.Success).data)
    }

    @Test
    fun testGetAlbumError() = runTest {
        coEvery { albumRepository.getAlbum(testAlbum.id!!) } returns DataState.Error(Exception(DEFAULT_ERROR))

        viewModel.getAlbum()
        advanceUntilIdle()

        val result = viewModel.albumResult.first()
        assertTrue(result is DataState.Error)
        assertEquals(DEFAULT_ERROR, (result as DataState.Error).error.message)
    }

    @Test
    fun testAddCommentSuccess() = runTest {
        // Given
        val albumId = testAlbum.id
        val description = "Amazing album!"
        val rating = 5
        val expectedComment = Comment(
            id = 0,
            description = description,
            rating = rating,
            collector = CollectorDTO(id = 1)
        )
        coEvery { albumRepository.addComment(albumId, expectedComment) } returns DataState.Success(Unit)

        // When
        viewModel.addComment(albumId, description, rating)
        advanceUntilIdle()

        // Then
        val result = viewModel.albumResult.first()
        assertTrue(result is DataState.Loading || result is DataState.Idle)
        assertEquals(DataState.Loading, viewModel.albumResult.value)
    }

    @Test
    fun testAddCommentError() = runTest {
        // Given
        val albumId = testAlbum.id
        val description = "Amazing album!"
        val rating = 5
        val errorMessage = "Error adding comment"
        val expectedComment = Comment(
            id = 0,
            description = description,
            rating = rating,
            collector = CollectorDTO(id = 1)
        )
        coEvery { albumRepository.addComment(albumId, expectedComment) } returns DataState.Error(Exception(DEFAULT_ERROR))

        // When
        viewModel.addComment(albumId, description, rating)
        advanceUntilIdle()

        // Then
        val loadingState = viewModel.albumResult.first()
        assertTrue(loadingState is DataState.Loading)
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
