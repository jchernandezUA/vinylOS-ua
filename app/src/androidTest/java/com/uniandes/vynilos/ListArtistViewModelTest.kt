package com.uniandes.vynilos

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.repository.ArtistRepository
import com.uniandes.vynilos.model.ARTIST_LIST
import com.uniandes.vynilos.model.DEFAULT_ERROR
import com.uniandes.vynilos.presentation.viewModel.ListArtistViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ListArtistViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val artistRepository: ArtistRepository = mockk()
    private lateinit var viewModel: ListArtistViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ListArtistViewModel(artistRepository)
    }

    @Test
    fun testGetArtistsSuccess() = runTest {
        val artistList = ARTIST_LIST
        coEvery { artistRepository.getArtists() } returns DataState.Success(artistList)

        viewModel.getArtists()

        val result = viewModel.artistResult.first()
        assertTrue(result is DataState.Success)
        assertEquals(artistList, (result as DataState.Success).data)
    }

    @Test
    fun testGetArtistsError() = runTest {
        val errorMessage = DEFAULT_ERROR
        coEvery { artistRepository.getArtists() } returns DataState.Error(Exception(errorMessage))

        viewModel.getArtists()

        val result = viewModel.artistResult.first()
        assertTrue(result is DataState.Error)
        assertEquals(errorMessage, (result as DataState.Error).error.message)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
