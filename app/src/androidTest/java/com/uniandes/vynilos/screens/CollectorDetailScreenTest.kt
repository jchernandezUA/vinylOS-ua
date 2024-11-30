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
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class CollectorDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Mock del ViewModel
    private val mockViewModel = mockk<CollectorDetailViewModel>(relaxed = true)

    // Datos simulados
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
        // Configura el Dispatcher principal para pruebas
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        // Resetea el Dispatcher principal al finalizar las pruebas
        Dispatchers.resetMain()
    }

    @Test
    fun testErrorState() = runTest {
        // Simula el estado de error
        val errorState: StateFlow<DataState<Pair<Collector, List<CollectorAlbum>>>> =
            MutableStateFlow(DataState.Error(Exception("Mock error")))

        // Configura el comportamiento del ViewModel simulado
        coEvery { mockViewModel.collectorState } returns errorState
        coEvery { mockViewModel.getCollector() } returns Unit

        // Configura el contenido de la pantalla
        composeTestRule.setContent {
            CollectorDetailScreen(viewModel = mockViewModel, navigationActions = NavigationActions())
        }

        // Verifica que el mensaje de error se muestre
        composeTestRule.onNodeWithText("Mock error").assertIsDisplayed()
    }

    @Test
    fun testSuccessState() = runTest(timeout = 20.seconds) { // Aumenta el tiempo de espera a 20 segundos
        // Simula el estado de éxito en el flujo de collectorState
        val successState: StateFlow<DataState<Pair<Collector, List<CollectorAlbum>>>> =
            MutableStateFlow(DataState.Success(Pair(mockCollector, mockCollectorAlbums)))

        // Configuración del mock del ViewModel
        coEvery { mockViewModel.collectorState } returns successState
        coEvery { mockViewModel.getCollector() } returns Unit

        // Configura el contenido del Composable
        composeTestRule.setContent {
            CollectorDetailScreen(viewModel = mockViewModel, navigationActions = NavigationActions())
        }

        // Verifica que los datos simulados se muestren en la pantalla
        composeTestRule.onNodeWithText("Mock Collector").assertIsDisplayed()
        composeTestRule.onNodeWithText("mock@example.com").assertIsDisplayed()
        composeTestRule.onNodeWithText("123-456-7890").assertIsDisplayed()
    }
}
