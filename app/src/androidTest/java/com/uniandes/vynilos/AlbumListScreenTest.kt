package com.uniandes.vynilos

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.repository.AlbumRepository
import com.uniandes.vynilos.data.repository.ArtistRepository
import com.uniandes.vynilos.presentation.navigation.HomeNavigation
import com.uniandes.vynilos.presentation.viewModel.ListAlbumViewModel
import com.uniandes.vynilos.presentation.viewModel.ListArtistViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AlbumListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val artistRepository: ArtistRepository = mockk()
    private val albumRepository: AlbumRepository = mockk()

    @Before
    fun setUp() {
        setMockResponseAlbums(DataState.Success(emptyList()))
        composeTestRule.setContent {
            HomeNavigation(
                ListArtistViewModel(artistRepository),
                ListAlbumViewModel(albumRepository)
            )
        }
    }

    private fun setMockResponseAlbums(response: DataState<List<Album>>) {
        coEvery { albumRepository.getAlbums() } returns response
    }

    @Test
    fun testLoadEmptyAlbumScreen() {
        // Given
        coEvery { albumRepository.getAlbums() } returns DataState.Success(listOf())

        // When
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.albums)).performClick()

        // Then
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.MensajeListaAlbumVacia)).assertIsDisplayed()
    }
}
