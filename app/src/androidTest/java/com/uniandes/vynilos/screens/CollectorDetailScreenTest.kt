package com.uniandes.vynilos.screens
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Collector
import com.uniandes.vynilos.data.model.CollectorAlbum
import com.uniandes.vynilos.data.repository.CollectorRepository
import com.uniandes.vynilos.model.DEFAULT_ERROR
import com.uniandes.vynilos.presentation.navigation.NavigationActions
import com.uniandes.vynilos.presentation.ui.screen.collector.CollectorDetailScreen
import com.uniandes.vynilos.presentation.viewModel.CollectorDetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CollectorDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val collectorRepository: CollectorRepository = mockk()
    private val mockCollector = Collector(
        id = 1,
        name = "Mock Collector",
        email = "mock@example.com",
        telephone = "123-456-7890",
        favoritePerformers = listOf()
    )
    val viewModel = CollectorDetailViewModel(
        mockCollector,
        collectorRepository
    )

    private val mockCollectorAlbums = listOf<CollectorAlbum>()

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testErrorState() {
        val errorState = DataState.Error(Exception(
            DEFAULT_ERROR
        ))

        coEvery { collectorRepository.getCollector(any()) } returns errorState
        coEvery { collectorRepository.getCollectorAlbum(any()) } returns errorState

        composeTestRule.setContent {
            CollectorDetailScreen(viewModel = viewModel, navigationActions = NavigationActions())
        }

        composeTestRule.onNodeWithText(DEFAULT_ERROR).assertIsDisplayed()
    }

    @Test
    fun testSuccessState() {
        val successStateCollector = DataState.Success(mockCollector)
        val successStateCollectorAlbums = DataState.Success(mockCollectorAlbums)

        coEvery { collectorRepository.getCollector(any()) } returns successStateCollector
        coEvery { collectorRepository.getCollectorAlbum(any()) } returns successStateCollectorAlbums

        composeTestRule.setContent {
            CollectorDetailScreen(viewModel = viewModel, navigationActions = NavigationActions())
        }

        composeTestRule.onNodeWithText("Mock Collector").assertIsDisplayed()
        composeTestRule.onNodeWithText("mock@example.com").assertIsDisplayed()
        composeTestRule.onNodeWithText("123-456-7890").assertIsDisplayed()
    }
}
