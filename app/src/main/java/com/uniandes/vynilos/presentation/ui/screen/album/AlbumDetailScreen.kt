package com.uniandes.vynilos.presentation.ui.screen.album

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uniandes.vynilos.R
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.Tracks
import com.uniandes.vynilos.presentation.navigation.ActionType
import com.uniandes.vynilos.presentation.navigation.NavigationActions
import com.uniandes.vynilos.presentation.ui.component.TextField
import com.uniandes.vynilos.presentation.ui.theme.VynilOSTheme
import com.uniandes.vynilos.presentation.viewModel.album.AlbumViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
                when (val result = albumResult){
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
                            text = result.error.message,
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
    
    var comments by remember { mutableStateOf(album.comments.map { it.description to it.rating  }) }

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
            HorizontalDivider()
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
        if(isCollector) {
            item {
                CommentsSection(
                    viewModel,
                    comments = comments
                )
            }
        }
    }
}

@Composable
fun CommentsSection(
    viewModel: AlbumViewModel,
    comments: List<Pair<String, Int>>
) {
    val isAddingComment by viewModel.isAddingComment.collectAsState()
    val newComment by viewModel.newComment.collectAsState()
    val newRating by viewModel.newRating.collectAsState()
    val commentError by viewModel.commentError.collectAsState()
    val isButtonEnabled by viewModel.isCommentButtonEnabled.collectAsState()

    Column {
        // Title
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
                onClick = {
                    viewModel.toggleCommentSection()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_comments),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Comment box
        if (isAddingComment) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                TextField(
                    value = newComment,
                    onValueChange = { viewModel.setComment(it) },
                    label = stringResource(R.string.add_comments),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textError = commentError
                )
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
                    Row {
                        repeat(5) { index ->
                            IconButton(
                                onClick = { viewModel.setRating(index + 1) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (index < newRating) MaterialTheme.colorScheme.primary else Color.Gray
                                )
                            }
                        }
                    }
                }
                Button(
                    onClick = {
                        viewModel.addComment()
                    },
                    modifier = Modifier.align(Alignment.End),
                    enabled = isButtonEnabled
                ) {
                    Text(stringResource(R.string.send))
                }
            }
        }

        // List of existing comments
        comments.reversed().forEach { (description, rating) ->
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
