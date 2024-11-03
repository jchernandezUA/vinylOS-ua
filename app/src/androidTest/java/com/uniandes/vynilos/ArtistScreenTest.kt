package com.uniandes.vynilos

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.Artist
import com.uniandes.vynilos.data.repository.AlbumRepository
import com.uniandes.vynilos.data.repository.ArtistRepository
import com.uniandes.vynilos.model.ARTIST_LIST
import com.uniandes.vynilos.model.DEFAULT_ERROR
import com.uniandes.vynilos.presentation.navigation.HomeNavigation
import com.uniandes.vynilos.presentation.viewModel.ListAlbumViewModel
import com.uniandes.vynilos.presentation.viewModel.ListArtistViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ArtistScreenTest {

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

    private fun setMockResponse(response: DataState<List<Artist>>) {
        coEvery { artistRepository.getArtists() } returns response
    }

    private fun setMockResponseAlbums(response: DataState<List<Album>>) {
        coEvery { albumRepository.getAlbums() } returns response
    }

    @Test
    fun testBottomBarDisplaysAllItems() {
        arrayOf(
            R.string.artists,
            R.string.albums,
            R.string.collectors
        ).forEach {
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(it)).assertIsDisplayed()
        }
    }

    @Test
    fun testLoadArtistScreen() {
        setMockResponse(DataState.Success(ARTIST_LIST))
        navigateAndCheckText(R.string.artists, ARTIST_LIST[0].name)
    }

    @Test
    fun testLoadEmptyArtistScreen() {
        setMockResponse(DataState.Success(emptyList()))
        navigateAndCheckText(R.string.artists, R.string.no_artist)
    }

    @Test
    fun testErrorLoadArtistScreen() {
        setMockResponse(DataState.Error(Exception(DEFAULT_ERROR)))
        navigateAndCheckText(R.string.artists, DEFAULT_ERROR)
    }

    @Test
    fun testArtistDetailsNavigation() {
        setMockResponse(DataState.Success(ARTIST_LIST))
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.artists)).performClick()
        composeTestRule.onNodeWithText(ARTIST_LIST[0].name).performClick()
        composeTestRule.onNodeWithText(ARTIST_LIST[0].name).assertIsDisplayed()
    }

    private fun navigateAndCheckText(buttonTextResId: Int, expectedTextResId: Int) {
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(buttonTextResId)).performClick()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(expectedTextResId)).assertIsDisplayed()
    }

    private fun navigateAndCheckText(buttonTextResId: Int, expectedText: String) {
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(buttonTextResId)).performClick()
        composeTestRule.onNodeWithText(expectedText).assertIsDisplayed()
    }
}
