import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Collector
import com.uniandes.vynilos.data.model.CollectorAlbum
import com.uniandes.vynilos.presentation.navigation.NavigationActions
import com.uniandes.vynilos.presentation.ui.screen.collector.CollectorDetailScreen
import com.uniandes.vynilos.presentation.viewModel.CollectorDetailViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class CollectorDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel = mockk<CollectorDetailViewModel>()

    private val mockCollector = Collector(
        id = 1,
        name = "Mock Collector",
        email = "mock@example.com",
        telephone = "123-456-7890",
        favoritePerformers = listOf()
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
    fun testErrorState() = runTest {
        val errorState: StateFlow<DataState<Pair<Collector, List<CollectorAlbum>>>> =
            MutableStateFlow(DataState.Error(Exception("Mock error")))

        coEvery { mockViewModel.collectorState } returns errorState
        coEvery { mockViewModel.getCollector() } returns Unit

        composeTestRule.setContent {
            CollectorDetailScreen(viewModel = mockViewModel, navigationActions = NavigationActions())
        }

        composeTestRule.onNodeWithText("Mock error").assertIsDisplayed()
    }

    @Test
    fun testSuccessState() = runTest {
        val successState: StateFlow<DataState<Pair<Collector, List<CollectorAlbum>>>> =
            MutableStateFlow(DataState.Success(Pair(mockCollector, mockCollectorAlbums)))

        coEvery { mockViewModel.collectorState } returns successState
        coEvery { mockViewModel.getCollector() } returns Unit

        composeTestRule.setContent {
            CollectorDetailScreen(viewModel = mockViewModel, navigationActions = NavigationActions())
        }

        composeTestRule.onNodeWithText("Mock Collector").assertIsDisplayed()
        composeTestRule.onNodeWithText("mock@example.com").assertIsDisplayed()
        composeTestRule.onNodeWithText("123-456-7890").assertIsDisplayed()
    }
}
