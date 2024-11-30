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
import com.uniandes.vynilos.data.repository.CollectorRepository
import com.uniandes.vynilos.model.DEFAULT_ERROR
import com.uniandes.vynilos.presentation.navigation.NavigationActions
import com.uniandes.vynilos.presentation.ui.screen.collector.CollectorDetailScreen
import com.uniandes.vynilos.presentation.viewModel.CollectorDetailViewModel
import kotlinx.coroutines.flow.first
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class CollectorDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    val collectorRepository: CollectorRepository = mockk()
    // Datos simulados
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
        // Configura el Dispatcher principal para pruebas
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        // Resetea el Dispatcher principal al finalizar las pruebas
        Dispatchers.resetMain()
    }

    @Test
    fun testErrorState() {
        // Simula el estado de error
        val errorState = DataState.Error(Exception(
            DEFAULT_ERROR
        ))

        // Configura el comportamiento del ViewModel simulado
        coEvery { collectorRepository.getCollector(any()) } returns errorState
        coEvery { collectorRepository.getCollectorAlbum(any()) } returns errorState

        // Configura el contenido de la pantalla
        composeTestRule.setContent {
            CollectorDetailScreen(viewModel = viewModel, navigationActions = NavigationActions())
        }

        // Verifica que el mensaje de error se muestre
        composeTestRule.onNodeWithText(DEFAULT_ERROR).assertIsDisplayed()
    }

    @Test
    fun testSuccessState() { // Aumenta el tiempo de espera a 20 segundos
        // Simula el estado de éxito en el flujo de collectorState
        val successStateCollector = DataState.Success(mockCollector)
        val successStateCollectorAlbums = DataState.Success(mockCollectorAlbums)

        // Configuración del mock del ViewModel
        coEvery { collectorRepository.getCollector(any()) } returns successStateCollector
        coEvery { collectorRepository.getCollectorAlbum(any()) } returns successStateCollectorAlbums

        // Configura el contenido del Composable
        composeTestRule.setContent {
            CollectorDetailScreen(viewModel = viewModel, navigationActions = NavigationActions())
        }

        // Verifica que los datos simulados se muestren en la pantalla
        composeTestRule.onNodeWithText("Mock Collector").assertIsDisplayed()
        composeTestRule.onNodeWithText("mock@example.com").assertIsDisplayed()
        composeTestRule.onNodeWithText("123-456-7890").assertIsDisplayed()
    }
}
