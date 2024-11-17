package com.uniandes.vynilos.screens

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.activity.ComponentActivity
import com.uniandes.vynilos.R
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Artist
import com.uniandes.vynilos.data.repository.ArtistRepository
import com.uniandes.vynilos.model.DEFAULT_ERROR
import com.uniandes.vynilos.model.createArtist
import com.uniandes.vynilos.presentation.ui.screen.artist.ArtistDetailScreen
import com.uniandes.vynilos.presentation.viewModel.ArtistViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class ArtistDetailScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val artistRepository: ArtistRepository = mockk()
    private val artist = createArtist()
    private val viewModel = ArtistViewModel(artist, artistRepository)

    private fun initWithDataState(dataState: DataState<Artist>) {
        coEvery { artistRepository.getArtist(1) } returns dataState
        composeTestRule.setContent {
            ArtistDetailScreen(viewModel = viewModel)
        }
    }

    // 1. Test for loading state
    @Test
    fun artistDetailScreen_displaysLoadingState() {
        //given
        val dataState = DataState.Loading
        //when
        initWithDataState(dataState)
        //then
        composeTestRule.onNodeWithTag("CircularProgressIndicator").assertExists()
    }

    // 2. Test for successful artist loading
    @Test
    fun artistDetailScreen_displaysArtistInfoAndAlbums() {

        //given
        val dataState = DataState.Success(artist)
        //when
        initWithDataState(dataState)
        //then
        composeTestRule.onAllNodesWithText(artist.name).assertCountEquals(2)

        // then
        // Verify first 3 albums are displayed
        artist.albums.slice(IntRange(0,3)).forEach {
            composeTestRule.onNodeWithText(it.name).assertIsDisplayed()
        }
    }

    // 3. Test for error state
    @Test
    fun artistDetailScreen_displaysErrorMessage() {
        val fakeError = DataState.Error(Exception(DEFAULT_ERROR))

        initWithDataState(fakeError)

        // Verify error message is displayed
        composeTestRule.onNodeWithText(fakeError.error.message).assertIsDisplayed()

        // Verify retry button is displayed
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.try_again)).assertIsDisplayed()
    }

    // 4. Test for artist with no albums
    @Test
    fun artistDetailScreen_displaysNoAlbumsMessage() {
        val albumDataState = DataState.Success(artist.copy(albums = emptyList()))
        initWithDataState(albumDataState)
        // Verify "no albums" message is displayed
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.no_albums_artist)).assertIsDisplayed()
    }
}
