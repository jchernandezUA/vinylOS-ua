package com.uniandes.vynilos.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.uniandes.vynilos.R
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.repository.AlbumRepository
import com.uniandes.vynilos.model.createAlbum
import com.uniandes.vynilos.presentation.navigation.ActionType
import com.uniandes.vynilos.presentation.navigation.AlbumActions
import com.uniandes.vynilos.presentation.navigation.NavigationActions
import com.uniandes.vynilos.presentation.ui.screen.album.AddAlbumScreen
import com.uniandes.vynilos.presentation.viewModel.album.AddAlbumViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class AddAlbumScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val albumRepository: AlbumRepository = mockk()
    private val viewModel = AddAlbumViewModel(albumRepository)
    private var action: ActionType? = null

    @Before
    fun setUp() {
        composeTestRule.setContent {
            AddAlbumScreen(
                viewModel = viewModel,
                navigationActions = NavigationActions {
                    action = it
                }
            )
        }
    }

    private fun initValidValues(album: Album, genre: String, recordLabel: String) {
        composeTestRule.apply {
            onNodeWithText(activity.getString(R.string.album))
                .performTextInput(album.name)

            onNodeWithText(activity.getString(R.string.description))
                .performTextInput(album.description)

            onNodeWithText(activity.getString(R.string.image))
                .performTextInput(album.cover)

            onNodeWithText(activity.getString(R.string.release_date))
                .performClick()

            onNodeWithText(activity.getString(R.string.select))
                .performClick()

            onNodeWithText(activity.getString(R.string.genre))
                .performClick()
            onNodeWithText(
                genre
            ).performClick()

            onNodeWithText(activity.getString(R.string.record_label))
                .performClick()

            onNodeWithText(
                recordLabel
            ).performClick()
        }

    }

    @Test
    fun testValidAlbumShowingFloatingActionButton() {
        // Given
        val recordLabel = composeTestRule.activity.resources.getStringArray(R.array.record_labels)[0]
        val genre = composeTestRule.activity.resources.getStringArray(R.array.genres)[0]
        val album = createAlbum(
            genre = genre,
            recordLabel = recordLabel
        )

        // When
        initValidValues(album, genre, recordLabel)

        //then
        assertEquals(album.name, viewModel.name.value)
        assertEquals(album.description, viewModel.description.value)
        assertEquals(album.cover, viewModel.cover.value)
        assertEquals(album.recordLabel, viewModel.recordLabel.value)
        assertEquals(album.genre, viewModel.genre.value)
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.add_album)
        ).isDisplayed()

    }

    @Test
    fun testSuccessAddAlbum() {
        // Given
        val recordLabel = composeTestRule.activity.resources.getStringArray(R.array.record_labels)[0]
        val genre = composeTestRule.activity.resources.getStringArray(R.array.genres)[0]
        val album = createAlbum(
            genre = genre,
            recordLabel = recordLabel
        )

        coEvery { albumRepository.addAlbum(any()) } returns DataState.Success(album.copy(id = 1))

        // When
        initValidValues(album, genre, recordLabel)
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.add_album)

        ).performClick()

        //then
        assertEquals(AlbumActions.OnAlbumAdded, action)
    }

    @Test
    fun testNoAlbumNameErrorDisplays() {
        // Given
        //page load
        //when clicks on album TextField
        composeTestRule.apply {
            onNodeWithText(
                activity.getString(R.string.album)
            ).performClick()

            //and then clicks on other TextField
            onNodeWithText(
                activity.getString(R.string.image)
            ).performClick()

            //then displays name album required field
            onNode(
                matcher = hasText(
                    activity.getString(R.string.required_field)
                ).and(
                    hasAnySibling(
                        hasText(activity.getString(R.string.album)
                        )
                    )
                )
            ).assertIsDisplayed()
        }
    }


    @Test
    fun testNoAlbumCoverErrorDisplays() {
        // Given
        //page load
        //when clicks on album TextField
        composeTestRule.apply {
            onNodeWithText(
                activity.getString(R.string.image)
            ).performClick()

            //and then clicks on other TextField
            onNodeWithText(
                activity.getString(R.string.album)
            ).performClick()

            //then displays name album required field
            onNode(
                matcher = hasText(
                    activity.getString(R.string.invalid_url)
                ).and(
                    hasAnySibling(hasText(activity.getString(R.string.image)))
                )
            ).assertIsDisplayed()
        }

    }

    @Test
    fun testNoAlbumDescriptionErrorDisplays() {
        // Given
        //page load
        //when clicks on album TextField
        composeTestRule.apply {
            onNodeWithText(
                activity.getString(R.string.description)
            ).performClick()

            //and then clicks on other TextField
            onNodeWithText(
                activity.getString(R.string.album)
            ).performClick()

            //then displays name album required field
            onNode(
                matcher = hasText(
                    activity.getString(R.string.required_field)
                ).and(
                    hasAnySibling(hasText(activity.getString(R.string.image)))
                )
            ).assertIsDisplayed()
        }
    }

    @Test
    fun testNoAlbumReleaseDateErrorDisplays() {
        // Given
        //page load
        //when clicks on album TextField
        composeTestRule.apply {
            onNodeWithText(
                composeTestRule.activity.getString(R.string.release_date)
            ).performClick()

            //and then clicks on other TextField
            onNodeWithText(
                activity.getString(R.string.cancel)
            ).performClick()

            //then displays name album required field
            onNode(
                matcher = hasText(
                    activity.getString(R.string.required_field)
                ).and(
                    hasAnySibling(hasText(activity.getString(R.string.release_date)))
                )
            ).assertIsDisplayed()
        }

    }

    @Test
    fun testNoAlbumGenreErrorDisplays() {
        // Given
        //page load
        //when clicks on album TextField
        composeTestRule.apply {
            onNodeWithText(
                activity.getString(R.string.genre)
            ).performClick()

            //and then clicks on other TextField
            onNodeWithText(
                activity.getString(R.string.album)
            ).performClick()

            //then displays name album required field
            onNode(
                matcher = hasText(
                    activity.getString(R.string.required_field)
                ).and(
                    hasAnySibling(hasText(activity.getString(R.string.genre)))
                )
            ).assertIsDisplayed()
        }
    }

    @Test
    fun testNoAlbumRecordLabelErrorDisplays() {
        // Given
        //page load
        //when clicks on album TextField
        composeTestRule.apply {
            onNodeWithText(
                activity.getString(R.string.record_label)
            ).performClick()

            //and then clicks on other TextField
            onNodeWithText(
                activity.getString(R.string.album)
            ).performClick()

            //then displays name album required field
            onNode(
                matcher = hasText(
                    activity.getString(R.string.required_field)
                ).and(
                    hasAnySibling(hasText(activity.getString(R.string.record_label)))
                )
            ).assertIsDisplayed()
        }
    }
}