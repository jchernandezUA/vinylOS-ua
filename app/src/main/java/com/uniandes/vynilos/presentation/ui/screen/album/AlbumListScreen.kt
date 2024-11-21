package com.uniandes.vynilos.presentation.ui.screen.album

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.uniandes.vynilos.R
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.common.NetworkModule
import com.uniandes.vynilos.common.ObserveAsActions
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.repository.AlbumRepositoryImpl
import com.uniandes.vynilos.presentation.navigation.AlbumActions
import com.uniandes.vynilos.presentation.navigation.NavigationActions
import com.uniandes.vynilos.presentation.ui.theme.VynilOSTheme
import com.uniandes.vynilos.presentation.viewModel.album.ListAlbumViewModel


@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun AlbumListScreen(
    modifier: Modifier = Modifier,
    viewModel: ListAlbumViewModel,
    isCollector: Boolean = false,
    showSnackBar: Boolean = false,
    navigationActions: NavigationActions = NavigationActions()
) {

    val albumsResult by viewModel.albumsResult.collectAsState()
    var errorMessage by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getAlbums()
    }

    if (showSnackBar) {
        viewModel.getAlbums()
        LaunchedEffect(Unit) {
            snackbarHostState.showSnackbar(
                message = context.getString(R.string.album_added)
            )
        }
    }

    viewModel.albumsResult.ObserveAsActions {
        if (it is DataState.Error) {
            errorMessage = it.error.message
        }
    }

    VynilOSTheme {
        Scaffold(
            modifier = modifier
                .fillMaxSize(),
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)  {
                    Snackbar(it)
                }
            }
        ) { _ ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 16.dp
                    )
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {

                    when (val result = albumsResult){
                        is DataState.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(80.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                        is DataState.Success -> {
                            val data = result.getDataOrNull()
                            if(!data.isNullOrEmpty()){
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    modifier = Modifier.fillMaxSize()
                                ){
                                    items(data){
                                            album ->
                                        AlbumsCard(album) {
                                            navigationActions.onAction(
                                                AlbumActions.OnClickAlbum(album)
                                            )
                                        }
                                    }
                                    item {
                                        if (isCollector)
                                            Spacer(modifier = Modifier.height(100.dp))
                                    }
                                }
                            }
                            else{
                                CardMessage(
                                    message = stringResource(R.string.empty_message_album)
                                )
                            }
                        }
                        is DataState.Error -> {
                            Text(
                                text = errorMessage,
                                color = Color.Red,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                        else -> {}
                    }
                }
                if (isCollector) {
                    FloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp),
                        onClick = {
                            navigationActions.onAction(
                                AlbumActions.OnClickAddAlbum
                            )
                        },
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = stringResource(R.string.add_album)
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun CardMessage(message: String) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {

            Text(
                text = message,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun AlbumsCard(
    album: Album,
    onItemClick: () -> Unit
){
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                onItemClick()
            },
        colors = CardDefaults.cardColors(
            //containerColor = Color.LightGray
            containerColor = Color.Transparent
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            Image(
                painter = rememberAsyncImagePainter(album.cover),
                contentDescription = album.name,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Text(
                text = album.name,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@PreviewLightDark
@Composable
fun AlbumListScreenPreview() {
    val albumServiceAdapter = NetworkModule.albumServiceAdapter
    val albumRepository = AlbumRepositoryImpl(albumServiceAdapter)
    val viewModel = ListAlbumViewModel(
        albumRepository
    )
    AlbumListScreen(viewModel = viewModel)
}