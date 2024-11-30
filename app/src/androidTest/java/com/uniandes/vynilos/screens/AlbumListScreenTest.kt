package com.uniandes.vynilos.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.uniandes.vynilos.R
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.DTO
import com.uniandes.vynilos.data.remote.entity.AlbumResponse
import com.uniandes.vynilos.data.repository.AlbumRepository
import com.uniandes.vynilos.data.repository.ArtistRepository
import com.uniandes.vynilos.data.repository.CollectorRepository
import com.uniandes.vynilos.model.ALBUM_LIST
import com.uniandes.vynilos.model.DEFAULT_ERROR
import com.uniandes.vynilos.model.createAlbumList
import com.uniandes.vynilos.presentation.navigation.HomeNavigation
import com.uniandes.vynilos.presentation.viewModel.ListArtistViewModel
import com.uniandes.vynilos.presentation.viewModel.ListCollectorViewModel
import com.uniandes.vynilos.presentation.viewModel.album.ListAlbumViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class AlbumListScreenTest {

    companion object {
        fun changeToCollectorType(composeTestRule: AndroidComposeTestRule<*,*>) {
            composeTestRule.onNodeWithText(
                composeTestRule.activity.getString(R.string.visitor),
                ignoreCase = true
            ).performClick()
        }
    }

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val artistRepository: ArtistRepository = mockk()
    private val albumRepository: AlbumRepository = mockk()
    private val collectorRepository: CollectorRepository = mockk()

    private fun setUp(albumListResponse: DataState<List<Album>>, albumDetailResponse: AlbumResponse? = null) {
        setMockResponseAlbums(albumListResponse)
        albumDetailResponse?.let {
            setMockResponseAlbumById(it)
        }
        composeTestRule.setContent {
            HomeNavigation(
                ListArtistViewModel(artistRepository),
                ListAlbumViewModel(albumRepository),
                ListCollectorViewModel(collectorRepository)
            )
        }
    }

    private fun setMockResponseAlbums(response: DataState<List<Album>>) {
        coEvery { albumRepository.getAlbums() } returns response
    }

    private fun setMockResponseAlbumById(albumResponse: AlbumResponse) {
        coEvery { albumRepository.getAlbum(albumResponse.id) } returns DataState.Success(albumResponse.DTO())
    }

    @Test
    fun testLoadEmptyAlbumScreen() {
        // Given
        val testList = emptyList<Album>()
        setUp(DataState.Success(testList))

        // When
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.albums)).performClick()

        // Then
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.empty_message_album)).assertIsDisplayed()
    }

    @Test
    fun testLoadAlbumScreenWithAlbums() {
        // Given
        val testList = ALBUM_LIST
        setUp(DataState.Success(testList))
        // When
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.albums)
        ).performClick()

        // Then
        composeTestRule.onNodeWithText(
            testList.first().name
        ).assertIsDisplayed()
    }


    @Test
    fun testLoadAlbumScreenWithError() {
        // Given
        setUp(DataState.Error(Exception(DEFAULT_ERROR)))
        // When
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.albums)
        ).performClick()

        // Then
        composeTestRule.onNodeWithText(
            DEFAULT_ERROR
        ).assertIsDisplayed()
    }

    @Test
    fun testClickVisitorElement() {
        // Given
        val testList = createAlbumList()
        setUp(DataState.Success(testList))

        // When
        changeToCollectorType(composeTestRule)

        // Then
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.collectors),
            ignoreCase = true
        ).assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.add_album),
            ignoreCase = true
        ).assertIsDisplayed()
    }
}
