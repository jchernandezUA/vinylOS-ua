package com.uniandes.vynilos.presentation.ui.screen.album

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.uniandes.vynilos.R
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.presentation.navigation.ActionType
import com.uniandes.vynilos.presentation.navigation.NavigationActions
import com.uniandes.vynilos.presentation.ui.preview.PreviewViewModel
import com.uniandes.vynilos.presentation.ui.theme.VynilOSTheme
import com.uniandes.vynilos.presentation.ui.theme.VynilOSTopAppBarColors
import com.uniandes.vynilos.presentation.viewModel.AlbumViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Card
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.graphics.Color

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun AlbumDetalScreen(
    viewModel: AlbumViewModel,
    navigationActions: NavigationActions = NavigationActions()
){
    val albumResult = viewModel.albumResult.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getAlbum()
    }

    VynilOSTheme {
        Scaffold(
            topBar = {
                AlbumTopBar {
                    navigationActions.onAction(ActionType.OnBack)
                }
            }
        ){  paddingValues ->
            Box(Modifier.fillMaxSize()
                .padding(paddingValues)
            ){
                when (val result = albumResult.value){
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
                                onClick = { viewModel.getAlbum() }
                            ) {
                                Text(text = stringResource(R.string.try_again))
                            }
                        }
                    }
                    is DataState.Success -> {
                        AlbumDetailView(result.data)
                    }
                    else -> {}
                }
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlbumTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        colors = VynilOSTopAppBarColors()
    )
}


@Composable
private fun AlbumDetailView(album : Album){
    LazyColumn (
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Album Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = album.cover,
                    contentDescription = "Album Cover",
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }
        // Album Title and Date
        item {
            Column( modifier = Modifier
                .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = album.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 1.dp)
                )

                Text(
                    text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                        .format(Date(album.releaseDate)),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 30.dp)
                )
            }
        }

        // Record Label and Genre
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                // Discográfica
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Discográfica: ",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = album.recordLabel,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Género
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Género: ",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = album.genre,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Description
        item {
            ExpandableDescription(album.description)
        }

        // Songs Section
        item {
            Text(
                text = "Canciones:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if(album.tracks.isNotEmpty()) {
            items(album.tracks) { track ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = track.name,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = track.duration,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }else{
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
private fun ExpandableDescription(description: String) {
    var isExpanded by remember { mutableStateOf(false) }
    val maxLength = 150
    val shouldShowButton = description.length > maxLength

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = "Descripción:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = if (isExpanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis
        )

        if (shouldShowButton) {
            Text(
                text = if (isExpanded) "Ver menos" else "Ver más",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .clickable { isExpanded = !isExpanded }
                    .padding(top = 4.dp)
            )
        }
    }
}

@Preview
@Composable
private fun AlbumDetailErrorScreenPreview() {
    AlbumDetalScreen(
        PreviewViewModel.getAlbumViewModel(null)
    )
}


@Preview
@Composable
private fun AlbumDetailScreenPreview() {
    AlbumDetalScreen(
        PreviewViewModel.getAlbumViewModel()
    )
}