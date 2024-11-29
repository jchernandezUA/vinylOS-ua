package com.uniandes.vynilos.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.uniandes.vynilos.R
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.Collector
import com.uniandes.vynilos.data.repository.CollectorRepository
import com.uniandes.vynilos.model.DEFAULT_ERROR
import com.uniandes.vynilos.model.createCollectors
import com.uniandes.vynilos.presentation.navigation.NavigationActions
import com.uniandes.vynilos.presentation.ui.screen.album.AlbumDetailScreen
import com.uniandes.vynilos.presentation.ui.screen.collector.CollectorDetailScreen
import com.uniandes.vynilos.presentation.ui.theme.VynilOSTheme
import com.uniandes.vynilos.presentation.viewModel.CollectorDetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class CollectorDetailScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val collectorRepository: CollectorRepository = mockk()
    private val collector = createCollectors()
    private val viewModel = CollectorDetailViewModel(
        collector, collectorRepository)


    private fun initWithDataState(dataState: DataState<Collector>) {
        coEvery { collectorRepository.getCollector(1) } returns dataState
        composeTestRule.setContent {
            VynilOSTheme {
                CollectorDetailScreen(
                    viewModel = viewModel,
                    navigationActions = NavigationActions()
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
        val collector = collector
        //when
        initWithDataState(DataState.Success(collector))
        // then
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.Email))
            .isDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.telephone))
            .isDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.favoritePerformers))
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
}