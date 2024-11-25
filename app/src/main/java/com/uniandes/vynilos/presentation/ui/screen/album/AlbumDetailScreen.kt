package com.uniandes.vynilos.presentation.ui.screen.album

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uniandes.vynilos.R
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.Tracks
import com.uniandes.vynilos.presentation.navigation.ActionType
import com.uniandes.vynilos.presentation.navigation.NavigationActions
import com.uniandes.vynilos.presentation.ui.theme.VynilOSTheme
import com.uniandes.vynilos.presentation.viewModel.album.AlbumViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.uniandes.vynilos.data.model.Performer
import com.uniandes.vynilos.presentation.ui.theme.vynilOSTopAppBarColors

@Composable
fun AlbumDetailScreen(
    viewModel: AlbumViewModel,
    isCollector: Boolean,
    navigationActions: NavigationActions = NavigationActions()
) {
    val albumResult by viewModel.albumResult.collectAsState()

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
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ){
                when (val result = albumResult.value){
                    is DataState.Loading -> {
                        CircularProgressIndicator(
                            Modifier
                                .size(100.dp)
                                .align(Alignment.Center)
                                .testTag("CircularProgressIndicator")
                        )
                    }
                    is DataState.Error -> {
                        Text(
                            text = result.error.message
                                ?: stringResource(R.string.error_loading_album),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    is DataState.Success -> {
                        AlbumDetailView(
                            album = result.data,
                            isCollector = isCollector,
                            viewModel = viewModel
                        )
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
        title = {
            Text(text = stringResource(R.string.album))
        },
        navigationIcon = {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        }
    )
}

@Composable
private fun AlbumDetailView(
    album: Album,
    isCollector: Boolean,
    viewModel: AlbumViewModel
) {
    var showAddTrackForm by remember { mutableStateOf(false) }
    val addTrackResult by viewModel.addTrackResult.collectAsState()

    LaunchedEffect(addTrackResult) {
        if (addTrackResult is DataState.Success) {
            viewModel.getAlbum()
            showAddTrackForm = false
            viewModel.resetAddTrackResult()
        }
    }
    
    val ctx = LocalContext.current
    var comments by remember { mutableStateOf(album.comments.map { it.description to it.rating  }) }
    var newComment by remember { mutableStateOf("") }
    var isAddingComment by remember { mutableStateOf(false) }
    var newRating by remember { mutableStateOf(0) }

    LazyColumn (
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = album.name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    .format(Date(album.releaseDate)),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "${stringResource(R.string.genre)}: ${album.genre}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Divider()
        }

        if (isCollector) {
            item {
                Button(
                    onClick = { showAddTrackForm = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(text = stringResource(R.string.add_track))
                }
            }

            if (showAddTrackForm) {
                item {
                    AddTrackForm(
                        onTrackAdded = {
                        },
                        onCancel = { showAddTrackForm = false },
                        viewModel = viewModel
                    )
                }
            }
        }

        item {
            Text(
                text = stringResource(R.string.songs),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        if (album.tracks.isNotEmpty()) {
            items(album.tracks) { track ->
                TrackCard(track = track)
            }
        } else {
            item {
                Text(
                    text = stringResource(R.string.no_tracks),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        // Sección de Comentarios
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.comments),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { isAddingComment = !isAddingComment }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_comments),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        if (isAddingComment) {
            item {
                Column {
                    // TextField para ingresar el comentario
                    TextField(
                        value = newComment,
                        onValueChange = { newComment = it },
                        label = { Text(stringResource(R.string.add_comments)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )

                    // Selector de estrellas (rating)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.select_rating),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        // Seleccionar el rating
                        Row {
                            repeat(5) { index ->
                                IconButton(
                                    onClick = { newRating = index + 1 }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (index < newRating) Color(0xFF613EEA) else Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    Button(
                        onClick = {
                            if (newComment.isNotBlank() && newRating > 0) {
                                viewModel.addComment(album.id, newComment, newRating)
                                comments = listOf(newComment to newRating) + comments
                                newComment = ""
                                newRating = 0
                                isAddingComment = false
                            }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(stringResource(R.string.send))
                    }
                }
            }
        }
        // Mostrar los comentarios existentes
        items(comments) { (description, rating) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        repeat(rating) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFF613EEA),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.description),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = album.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun TrackCard(track: Tracks) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = track.name,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = track.duration,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
