package com.uniandes.vynilos.presentation.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.uniandes.vynilos.R
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.common.NetworkModule
import com.uniandes.vynilos.common.observeAsActions
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.repository.AlbumRepositoryImpl
import com.uniandes.vynilos.presentation.ui.screen.ui.theme.VynilOSTheme
import com.uniandes.vynilos.presentation.viewModel.ListAlbumViewModel


@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun AlbumListScreen(
    viewModel: ListAlbumViewModel,
    ) {

    val albumsResult by viewModel.albumsResult.collectAsState()
    var errorMessage by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        viewModel.getAlbums()
    }

    viewModel.albumsResult.observeAsActions {
        if (it is DataState.Error) {
            errorMessage = it.error.message
        }
    }

    VynilOSTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 12.dp, top = 60.dp, end = 1.dp, bottom = 12.dp)
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {

                    when (val result = albumsResult){
                        is DataState.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(80.dp).align(Alignment.CenterHorizontally)
                            )
                        }
                        is DataState.Success -> {
                            val data = result.getDataOrNull()
                            if(!data.isNullOrEmpty()){
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    modifier = Modifier.fillMaxHeight()
                                ){
                                    items(data){
                                        album ->
                                        AlbumsCard(album)
                                    }
                                }
                            }
                            else{
                                // Mostrar mensaje cuando no hay álbumes disponibles
                                CardMensajes(mensaje = stringResource(R.string.MensajeListaAlbumVacia)
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
                            // quedo por defecto, teniendo en cuenta que el when necesita un else
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun CardMensajes(mensaje: String) {
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
                text = mensaje,
                fontSize = 18.sp,
                color = colorResource(R.color.purpleIcon),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun AlbumsCard(album : Album){
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
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            // Carga de la imagen usando Coil
            Image(
                painter = rememberAsyncImagePainter(album.cover),
                contentDescription = "Imagen del álbum",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            // Nombre del álbum
            Text(
                text = album.name,
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun AlbumListScreenPreview() {
    val albumServiceAdapter = NetworkModule.albumServiceAdapter
    val albumRepository = AlbumRepositoryImpl(albumServiceAdapter)
    val viewModel = ListAlbumViewModel(
        albumRepository
    )
    AlbumListScreen(viewModel)
}