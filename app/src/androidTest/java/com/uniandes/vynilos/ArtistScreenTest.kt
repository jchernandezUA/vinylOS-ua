package com.uniandes.vynilos

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.repository.ArtistRepository
import com.uniandes.vynilos.model.ARTIST_LIST
import com.uniandes.vynilos.model.DEFAULT_ERROR
import com.uniandes.vynilos.presentation.navigation.HomeNavigation
import com.uniandes.vynilos.presentation.viewModel.ListArtistViewModel
import com.uniandes.vynilos.utils.BaseComposeTest
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception


class ArtistScreenTest: BaseComposeTest() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val artistRepository: ArtistRepository = mockk()


    @Before
    fun setUp() {
        composeTestRule.setContent {
           // HomeNavigation(ListArtistViewModel(artistRepository))
        }
    }


    @Test
    fun testBottomBarDisplaysAllItems() {
        //Then
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.artists)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.albums)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.collectors)).assertIsDisplayed()
    }

    @Test
    fun testLoadArtistScreen() {

        //given
        val artistList = ARTIST_LIST
        coEvery { artistRepository.getArtists() } returns DataState.Success(artistList)

        //when
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.artists)).performClick()

        //then
        composeTestRule.onNodeWithText(artistList[0].name).assertIsDisplayed()

    }

    @Test
    fun testLoadEmptyArtistScreen() {

        //given
        coEvery { artistRepository.getArtists() } returns DataState.Success(listOf())

        //when
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.artists)).performClick()

        //then
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.no_artist)).assertIsDisplayed()
    }

    @Test
    fun testErrorLoadArtistScreen() {

        val errorMessage = DEFAULT_ERROR
        //given
        coEvery { artistRepository.getArtists() } returns DataState.Error(Exception(errorMessage))

        //when
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.artists)).performClick()

        //then
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }
}