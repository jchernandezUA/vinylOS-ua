package com.uniandes.vynilos.presentation.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.uniandes.vynilos.R
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.common.NetworkModule
import com.uniandes.vynilos.common.observeAsActions
import com.uniandes.vynilos.data.model.Artist
import com.uniandes.vynilos.data.repository.ArtistRepositoryImpl

import com.uniandes.vynilos.presentation.ui.theme.VynilOSTheme
import com.uniandes.vynilos.presentation.viewModel.ListArtistViewModel

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun ArtistScreen(
    viewModel: ListArtistViewModel,
    //navigationActions: NavigationActions = NavigationActions()
) {
    val artistsResult by viewModel.artistResult.collectAsState()

    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getArtists()
    }


    viewModel.artistResult.observeAsActions {
        if (it is DataState.Error) {
            errorMessage = it.error.message
        }
    }


    VynilOSTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Artistas",
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    when (val result = artistsResult) {
                        is DataState.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally)
                            )
                        }
                        is DataState.Success -> {
                            val data = result.getDataOrNull()
                            if (!data.isNullOrEmpty()) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    items(data) { artist ->
                                        ArtistCard(artist)
                                    }
                                }
                            } else {
                                Text(
                                    text = "No artists found",
                                    color = Color.Gray,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
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
                        else -> {
                            // En caso de que no haya ningún estado particular
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ArtistCard(artist: Artist) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val painter: Painter = rememberAsyncImagePainter(
                model = artist.image,
                placeholder = painterResource(id = R.drawable.placeholder_artist),
                error = painterResource(id = R.drawable.placeholder_artist)
            )
            Image(
                painter = painter,
                contentDescription = "Artist Image",
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 8.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = artist.name, color = Color.Black, style = androidx.compose.ui.text.TextStyle.Default)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = artist.description, color = Color.Gray)
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ArtistScreenPreview() {
    val artistServiceAdapter = NetworkModule.artistServiceAdapter
    val artistRepository = ArtistRepositoryImpl(artistServiceAdapter)
    val viewModel = ListArtistViewModel(
        artistRepository
    )
    ArtistScreen(viewModel)
}