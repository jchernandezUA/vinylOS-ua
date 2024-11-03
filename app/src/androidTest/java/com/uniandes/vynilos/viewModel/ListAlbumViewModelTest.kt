package com.uniandes.vynilos.viewModel

import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.repository.AlbumRepository
import com.uniandes.vynilos.model.ALBUM_LIST
import com.uniandes.vynilos.model.DEFAULT_ERROR
import com.uniandes.vynilos.presentation.viewModel.ListAlbumViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListAlbumViewModelTest {

    private val albumRepository: AlbumRepository = mockk()
    private lateinit var viewModel: ListAlbumViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ListAlbumViewModel(albumRepository)
    }

    @Test
    fun testGetAlbumsSuccess() = runTest {
        val albumList = ALBUM_LIST
        coEvery { albumRepository.getAlbums() } returns DataState.Success(albumList)

        viewModel.getAlbums()
        advanceUntilIdle()
        val result = viewModel.albumsResult.first()
        assertTrue(result is DataState.Success)
        assertEquals(albumList, (result as DataState.Success).data)
    }

    @Test
    fun testGetAlbumsError() = runTest {
        val errorMessage = DEFAULT_ERROR
        coEvery { albumRepository.getAlbums() } returns DataState.Error(Exception(errorMessage))

        viewModel.getAlbums()
        advanceUntilIdle()
        val result = viewModel.albumsResult.first()
        assertTrue(result is DataState.Error)
        assertEquals(errorMessage, (result as DataState.Error).error.message)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
