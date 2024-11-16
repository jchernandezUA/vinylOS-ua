package com.uniandes.vynilos.presentation.ui.screen.album

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uniandes.vynilos.R
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.presentation.navigation.ActionType
import com.uniandes.vynilos.presentation.navigation.NavigationActions
import com.uniandes.vynilos.presentation.ui.theme.VynilOSTheme
import com.uniandes.vynilos.presentation.ui.theme.VynilOSTopAppBarColors
import com.uniandes.vynilos.presentation.viewModel.AlbumViewModel


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
    ) {
        item{
            Text(
                text = album.name,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}