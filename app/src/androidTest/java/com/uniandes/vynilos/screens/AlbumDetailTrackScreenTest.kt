package com.uniandes.vynilos.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.activity.ComponentActivity
import com.uniandes.vynilos.R
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.Tracks
import com.uniandes.vynilos.data.repository.AlbumRepository
import com.uniandes.vynilos.presentation.navigation.NavigationActions
import com.uniandes.vynilos.presentation.ui.screen.album.AlbumDetailScreen
import com.uniandes.vynilos.presentation.viewModel.album.AlbumViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class AlbumDetailTrackScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val album = Album(
        id = 1,
        name = "Test Album",
        cover = "",
        releaseDate = 0L,
        description = "",
        genre = "",
        recordLabel = ""
    )

    @Test
    fun addTrackButtonVisibleForCollector(): Unit = runBlocking {
        val albumRepository = mockk<AlbumRepository>()
        val viewModel = AlbumViewModel(album, albumRepository)

        coEvery { albumRepository.getAlbum(album.id!!) } returns DataState.Success(album)

        viewModel.getAlbum()

        composeTestRule.setContent {
            AlbumDetailScreen(
                viewModel = viewModel,
                isCollector = true,
                navigationActions = NavigationActions()
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.add_track)
        ).assertIsDisplayed()
    }

    @Test
    fun addTrackButtonNotVisibleForVisitor() = runBlocking {
        val albumRepository = mockk<AlbumRepository>()
        val viewModel = AlbumViewModel(album, albumRepository)

        coEvery { albumRepository.getAlbum(album.id!!) } returns DataState.Success(album)

        viewModel.getAlbum()

        composeTestRule.setContent {
            AlbumDetailScreen(
                viewModel = viewModel,
                isCollector = false,
                navigationActions = NavigationActions()
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.add_track)
        ).assertDoesNotExist()
    }

    @Test
    fun addTrackFormIsDisplayedWhenAddTrackButtonClicked(): Unit = runBlocking {
        val albumRepository = mockk<AlbumRepository>()
        val viewModel = AlbumViewModel(album, albumRepository)

        coEvery { albumRepository.getAlbum(album.id!!) } returns DataState.Success(album)

        viewModel.getAlbum()

        composeTestRule.setContent {
            AlbumDetailScreen(
                viewModel = viewModel,
                isCollector = true,
                navigationActions = NavigationActions()
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.add_track)
        ).performClick()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.add_new_track)
        ).assertIsDisplayed()
    }

    @Test
    fun addingTrackSuccessfullyUpdatesTheAlbum(): Unit = runBlocking {
        val albumRepository = mockk<AlbumRepository>()
        val viewModel = AlbumViewModel(album, albumRepository)
        val newTrack = Tracks(name = "New Track", duration = "03:30")

        coEvery { albumRepository.getAlbum(album.id!!) } returns DataState.Success(album.copy(tracks = listOf(newTrack)))
        coEvery { albumRepository.addTrackToAlbum(album.id!!, any()) } returns DataState.Success(newTrack)

        viewModel.getAlbum()

        composeTestRule.setContent {
            AlbumDetailScreen(
                viewModel = viewModel,
                isCollector = true,
                navigationActions = NavigationActions()
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.add_track)
        ).performClick()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.track_name)
        ).performTextInput(newTrack.name)
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.track_duration)
        ).performTextInput(newTrack.duration)

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.add)
        ).performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText(newTrack.name).assertIsDisplayed()
    }

}
