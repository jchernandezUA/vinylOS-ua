package com.uniandes.vynilos.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.uniandes.vynilos.R
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.repository.AlbumRepository
import com.uniandes.vynilos.model.DEFAULT_ERROR
import com.uniandes.vynilos.model.createAlbum
import com.uniandes.vynilos.presentation.navigation.NavigationActions
import com.uniandes.vynilos.presentation.ui.screen.album.AlbumDetailScreen
import com.uniandes.vynilos.presentation.ui.theme.VynilOSTheme
import com.uniandes.vynilos.presentation.viewModel.album.AlbumViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class AlbumDetailScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val albumRepository:AlbumRepository = mockk()
    private val album = createAlbum()
    private val viewModel = AlbumViewModel(
        album, albumRepository
    )

    private fun initWithDataState(dataState: DataState<Album>) {
        coEvery { albumRepository.getAlbum(1) } returns dataState
        composeTestRule.setContent {
            VynilOSTheme {
                AlbumDetailScreen(
                    viewModel = viewModel,
                    navigationActions = NavigationActions(),
                    isCollector = true,
                )
            }
        }
    }

    @Test
    fun testLoadingIndicatorIsDisplayed() {
        //given
        initWithDataState(DataState.Loading)

        // then
        composeTestRule.onNodeWithTag("CircularProgressIndicator")
            .assertExists()
    }

    @Test
    fun testTracksAreRenderedCorrectly() {
        //given
        val album = album
        //when
        initWithDataState(DataState.Success(album))
        // then
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.description))
            .isDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.record_label))
            .isDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.songs))
            .isDisplayed()
    }


    @Test
    fun testErrorStateDisplaysErrorMessage() {
        //given
        val errorMessage = DEFAULT_ERROR

        //when
        initWithDataState(DataState.Error(Exception(errorMessage)))

        // then
        composeTestRule.onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }
    @Test
    fun testAddCommentButtonIsDisplayed() {
        // Given
        val album = album
        initWithDataState(DataState.Success(album))
        composeTestRule.waitForIdle()
        // Then
        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.add_comments))
            .performScrollTo()
            .assertIsDisplayed()
    }
    @Test
    fun testClickAddCommentButton() {
        // Given
        val album = album
        initWithDataState(DataState.Success(album))
        composeTestRule.waitForIdle()

        // When
        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.add_comments))
            .performScrollTo().performClick()

        // Then
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.add_comments))
            .assertIsDisplayed()
    }


    @Test
    fun testAlbumTitleIsDisplayed() {
        // Given
        val album = album
        initWithDataState(DataState.Success(album))

        // Then
        composeTestRule.onNodeWithText(album.name)
            .assertIsDisplayed()
    }
}
