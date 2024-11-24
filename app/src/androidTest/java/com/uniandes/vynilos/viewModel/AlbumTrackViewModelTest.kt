package com.uniandes.vynilos.viewModel

import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.Tracks
import com.uniandes.vynilos.data.repository.AlbumRepository
import com.uniandes.vynilos.presentation.viewModel.album.AlbumViewModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AlbumTrackViewModelTest {

    private val album = Album(
        id = 1,
        name = "Test Album",
        cover = "",
        releaseDate = 0L,
        description = "",
        genre = "",
        recordLabel = ""
    )

    private lateinit var albumRepository: AlbumRepository

    @Before
    fun setup() {
        albumRepository = mockk()
        coEvery { albumRepository.getAlbum(any()) } returns DataState.Success(album)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addTrackToAlbumSuccessfullyAddsTrack() = runTest {
        val track = Tracks(name = "New Track", duration = "03:30")

        coEvery { albumRepository.addTrackToAlbum(album.id!!, track) } returns DataState.Success(track)
        coEvery { albumRepository.getAlbum(any()) } returns DataState.Success(
            album.copy(tracks = listOf(track))
        )

        val viewModel = AlbumViewModel(album, albumRepository)
        viewModel.addTrackToAlbum(track.name, track.duration)

        advanceUntilIdle()

        val addTrackResult = viewModel.addTrackResult.first()
        assert(addTrackResult is DataState.Success)
        assertEquals(track, (addTrackResult as DataState.Success).data)

        val albumResult = viewModel.albumResult.first()
        assert(albumResult is DataState.Success)
        assertEquals(track, (albumResult as DataState.Success).data.tracks.first())
    }
}
