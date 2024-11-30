package com.uniandes.vynilos.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.uniandes.vynilos.R
import com.uniandes.vynilos.common.DataError
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Collector
import com.uniandes.vynilos.data.repository.AlbumRepository
import com.uniandes.vynilos.data.repository.ArtistRepository
import com.uniandes.vynilos.data.repository.CollectorRepository
import com.uniandes.vynilos.model.COLLECTOR_LIST
import com.uniandes.vynilos.model.DEFAULT_ERROR
import com.uniandes.vynilos.presentation.navigation.HomeNavigation
import com.uniandes.vynilos.presentation.viewModel.ListArtistViewModel
import com.uniandes.vynilos.presentation.viewModel.ListCollectorViewModel
import com.uniandes.vynilos.presentation.viewModel.album.ListAlbumViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CollectorListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val artistRepository: ArtistRepository = mockk()
    private val albumRepository: AlbumRepository = mockk()
    private val collectorRepository: CollectorRepository = mockk()

    @Before
    fun setUp() {
        coEvery { albumRepository.getAlbums() } returns DataState.Success(emptyList())
        composeTestRule.setContent {
            HomeNavigation(
                ListArtistViewModel(artistRepository),
                ListAlbumViewModel(albumRepository),
                ListCollectorViewModel(collectorRepository)
            )
        }
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.collectors)).performClick()
    }

    private fun setCollectorsListResponse(response : DataState<List<Collector>> ){
        coEvery { collectorRepository.getCollectors() } returns response
    }

    @Test
    fun testCollectorsListScreen() {
        //given
        val collectors = COLLECTOR_LIST
        setCollectorsListResponse(DataState.Success(collectors))

        //when
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.collectors)).performClick()

        //then
        composeTestRule.onNodeWithText(text = collectors[0].name).assertIsDisplayed()
        composeTestRule.onNodeWithText(text = collectors[0].email).assertIsDisplayed()
        composeTestRule.onNodeWithText(text = collectors[0].telephone).assertIsDisplayed()
    }


    @Test
    fun testCollectorsEmptyListScreen() {
        //given
        val collectors: List<Collector> = emptyList()
        setCollectorsListResponse(DataState.Success(collectors))

        //when
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.collectors)).performClick()

        //then
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.no_collectors)
        ).assertIsDisplayed()

    }

    @Test
    fun testCollectorListErrorScreen() {
        //given
        setCollectorsListResponse(
            DataState.Error(DataError(DEFAULT_ERROR))
        )

        //when
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.collectors)).performClick()

        //then
        composeTestRule.onNodeWithText(
            DEFAULT_ERROR
        ).assertIsDisplayed()

    }

}
