package com.uniandes.vynilos.presentation.ui.screen.album

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.uniandes.vynilos.data.model.Performer
import com.uniandes.vynilos.data.model.Tracks
import com.uniandes.vynilos.presentation.ui.theme.vynilOSTopAppBarColors
import com.uniandes.vynilos.presentation.viewModel.album.AlbumViewModel

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
                                .testTag("CircularProgressIndicator")
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
        colors = vynilOSTopAppBarColors()
    )
}


@Composable
private fun AlbumDetailView(album : Album){
    val ctx = LocalContext.current
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
                contentAlignment = Center
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = ctx.getString(R.string.record_label)+":",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = album.recordLabel,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = ctx.getString(R.string.genre)+": ",
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
                text = ctx.getString(R.string.songs)+":",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (album.tracks.isNotEmpty()) {
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    items(album.tracks) { track ->
                        TrackCard(track = track)
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

        // Songs Section
        item {
            Text(
                text = ctx.getString(R.string.artists)+": ",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 15.dp)
            )
        }

        if (album.performers.isNotEmpty()) {
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    items(album.performers) { performer ->
                        PerformerCard(performer = performer)
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

        // Comments Section
        item {
            Text(
                text = ctx.getString(R.string.comments)+":",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        if(album.comments.isNotEmpty()){

            items(album.comments) { comment ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .border(
                            width = 1.dp,
                            color = Color(0xFF613EEA)
                        ),
                    colors = CardDefaults.cardColors(
                        //containerColor = Color.LightGray
                        containerColor = Color.Transparent
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = comment.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            repeat(comment.rating) {
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
fun PerformerCard(
    performer: Performer
){
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            //containerColor = Color.LightGray
            containerColor = Color.Transparent
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {

            Image(
                painter = rememberAsyncImagePainter(performer.image),
                contentDescription = stringResource(R.string.artist_image),
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Text(
                text = performer.name,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}


@Composable
fun TrackCard(track: Tracks) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp)
            .border(
                width = 1.dp,
                color = Color.Black
            ),
        colors = CardDefaults.cardColors(
            //containerColor = Color.LightGray
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = "",
                tint = Color(0xFF613EEA),
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 8.dp)
            )

            Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = track.name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(R.string.duration, track.duration),
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )


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
            text = stringResource(R.string.description),
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
                text = stringResource(if (isExpanded) R.string.see_less  else R.string.see_more),
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
