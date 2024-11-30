package com.uniandes.vynilos.screens

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import com.uniandes.vynilos.R
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.repository.AlbumRepository
import com.uniandes.vynilos.di.albumModule
import com.uniandes.vynilos.di.artistModule
import com.uniandes.vynilos.di.collectorModule
import com.uniandes.vynilos.model.createAlbum
import com.uniandes.vynilos.presentation.activities.MainActivity
import com.uniandes.vynilos.screens.AlbumListScreenTest.Companion.changeToCollectorType
import io.mockk.coEvery
import org.junit.Before
import org.junit.Rule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.test.Test

class AddAlbumScreenTest : KoinTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        stopKoin()
        startKoin {
            modules(albumModule, artistModule, collectorModule)
        }
        coEvery { get<AlbumRepository>().getAlbums() } returns DataState.Success(emptyList())
        composeTestRule.activityRule.scenario.onActivity {
            it.startActivity(Intent(it, MainActivity::class.java))
        }
        changeToCollectorType(composeTestRule)
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.add_album),
            ignoreCase = true
        ).performClick()
        composeTestRule.waitUntil {
            composeTestRule.onNodeWithText(
                composeTestRule.activity.getString(R.string.album)
            ).isDisplayed()
        }
    }


    private fun setUpvValidAlbumInput(album: Album, genre: String, recordLabel: String) {
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
        setUpvValidAlbumInput(album, genre, recordLabel)

        //then
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.add_album)
        ).isDisplayed()
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
            ).performScrollTo().isDisplayed()
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
            ).performScrollTo().isDisplayed()
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
            ).performScrollTo().performClick()

            //and then clicks on other TextField
            onNodeWithText(
                activity.getString(R.string.cancel)
            ).performClick()

            composeTestRule
            composeTestRule
            composeTestRule
            composeTestRule
            composeTestRule
            //then displays name album required field
            onNode(
                matcher = hasText(
                    activity.getString(R.string.required_field)
                ).and(
                    hasAnySibling(hasText(activity.getString(R.string.release_date)))
                )
            ).performScrollTo().isDisplayed()
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
            ).performClick().performTextInput("Only for test")

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
            ).performClick().performTextInput("Only for test")


            //then displays name album required field
            onNode(
                matcher = hasText(
                    activity.getString(R.string.required_field)
                ).and(
                    hasAnySibling(hasText(activity.getString(R.string.record_label)))
                )
            ).performScrollTo().assertIsDisplayed()
        }
    }

    @org.junit.Test
    fun testClickShowAlbumAddedElement() {
        // Given
        val recordLabel = composeTestRule.activity.resources.getStringArray(R.array.record_labels)[0]
        val genre = composeTestRule.activity.resources.getStringArray(R.array.genres)[0]
        val album = createAlbum(
            genre = genre,
            recordLabel = recordLabel
        )

        // When
        setUpvValidAlbumInput(album, genre, recordLabel)

        coEvery { get<AlbumRepository>().addAlbum(any()) } returns DataState.Success(album)

        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.add_album),
            ignoreCase = true
        ).performClick()

        // Then very is already added
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.album_added),
            ignoreCase = true
        ).performClick()
    }
}