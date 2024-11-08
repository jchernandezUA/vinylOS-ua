package com.uniandes.vynilos.presentation.ui.screen.artist
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.uniandes.vynilos.R
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.common.timestampToYear
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.Artist
import com.uniandes.vynilos.data.model.DTO
import com.uniandes.vynilos.data.repository.ArtistRepositoryImpl
import com.uniandes.vynilos.presentation.navigation.ActionType
import com.uniandes.vynilos.presentation.navigation.NavigationActions
import com.uniandes.vynilos.presentation.ui.preview.PreviewModel
import com.uniandes.vynilos.presentation.ui.preview.PreviewViewModel
import com.uniandes.vynilos.presentation.ui.theme.VynilOSTheme
import com.uniandes.vynilos.presentation.ui.theme.VynilOSTopAppBarColors
import com.uniandes.vynilos.presentation.viewModel.ArtistViewModel


/*
ejemplo de como crear el viewModel
private val artistDetailViewModel: ArtistViewModel by viewModel {
    val artist = inteng.getSafeSerializableExtra(ARTIST)?:f
    parametersOf(artist)
}

*/

@Composable
fun ArtistDetailScreen(
    viewModel: ArtistViewModel,
    navigationActions: NavigationActions = NavigationActions()
) {

    val artistResult = viewModel.artistResult.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getArtist()
    }
    VynilOSTheme {

        Scaffold(
            topBar = {
                ArtistTopBar {
                    navigationActions.onAction(ActionType.OnBack)
                }
            }
        ) {  paddingValues ->
            Box(
                Modifier.fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (val result = artistResult.value) {
                    is DataState.Loading -> {
                        CircularProgressIndicator(
                            Modifier.size(100.dp)
                        )
                    }
                    is DataState.Error -> {
                        Column(
                            Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = result.error.message,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Button(
                                onClick = { viewModel.getArtist() }
                            ) {
                                Text(text = stringResource(R.string.try_again))
                            }
                        }
                    }
                    is DataState.Success -> {
                        ArtistDetailView(result.data)
                    }
                    else -> {}
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArtistTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = ""
                )
            }
        },
        colors = VynilOSTopAppBarColors()
    )
}

@Composable
private fun ArtistDetailView(artist: Artist) {
    LazyColumn (
        Modifier
            .fillMaxSize()
    ) {

        item {
            Column(
                Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = artist.name,
                    style = MaterialTheme.typography.headlineSmall,
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    verticalAlignment = CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(85.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(artist.image)
                                .build(),
                            placeholder = rememberVectorPainter(Icons.Filled.Person),
                            contentDescription = artist.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.clip(CircleShape)
                                .size(85.dp)
                        )
                    }

                    Spacer(Modifier.width(16.dp))

                    Text(text = artist.name)
                }
                Spacer(Modifier.height(10.dp))
            }
        }

        if (artist.albums.isNotEmpty()) {
            items(artist.albums) {
                CardAlbumsArtist(it)
                Spacer(Modifier.height(12.dp))
            }
        } else {
            item {
                Box(
                    Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.no_albums_artist),
                        color = Color.Gray,
                        modifier = Modifier.align(Center)
                    )
                }
            }
        }


    }
}

@Composable
private fun CardAlbumsArtist(album: Album) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp
            )
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 16.dp
                )
        ) {
            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(album.name)
                Text("${album.releaseDate.timestampToYear()}")
            }
        }
    }
}


@Preview
@Composable
private fun ArtistDetailScreenPreview() {
    ArtistDetailScreen(
        PreviewViewModel.getArtistViewModel()
    )
}

@Preview
@Composable
private fun ArtistDetailEmptyAlbumScreenPreview() {
    ArtistDetailScreen(
        PreviewViewModel.getArtistViewModel(
            PreviewModel.artistEmptyValues
        )
    )
}

@Preview
@Composable
private fun ArtistDetailErrorScreenPreview() {
    ArtistDetailScreen(
        PreviewViewModel.getArtistViewModel(null)
    )
}