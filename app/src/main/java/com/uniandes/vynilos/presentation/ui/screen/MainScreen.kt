package com.uniandes.vynilos.presentation.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.common.NetworkModule
import com.uniandes.vynilos.common.observeAsActions
import com.uniandes.vynilos.data.repository.AlbumRepositoryImpl
import com.uniandes.vynilos.presentation.navigation.ActionType
import com.uniandes.vynilos.presentation.navigation.NavigationActions
import com.uniandes.vynilos.presentation.ui.theme.VynilOSTheme
import com.uniandes.vynilos.presentation.viewModel.ListAlbumViewModel


@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun MainScreen(
    viewModel: ListAlbumViewModel,
    navigationActions: NavigationActions = NavigationActions()
) {
    val albumsResult by viewModel.albumsResult.collectAsState()

    var errorMessage by remember { mutableStateOf("") }

    viewModel.albumsResult.observeAsActions {
        if (it is DataState.Error) {
            errorMessage = it.error.message
        }
    }

    VynilOSTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { _ ->

            Column {

                Button(
                    onClick = { viewModel.getAlbums() }
                ) {
                    Text(text = "Get albums")
                }
                Button(
                    onClick = {
                        navigationActions.onAction(ActionType.CLICK_NOT_MAIN)
                    }) {
                    Text(text = "Move to not main")
                }

                when(val result = albumsResult) {
                    is DataState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(100.dp)
                        )
                    }
                    is DataState.Success -> {
                        val data =  result.getDataOrNull()
                        data?.forEach {
                           Text(text = it.name)
                       }
                    }
                    else -> {
                        Text(
                            text = errorMessage,
                            color = Color.Red
                        )
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val albumServiceAdapter = NetworkModule.albumServiceAdapter
    val albumRepository = AlbumRepositoryImpl(albumServiceAdapter)
    val viewModel = ListAlbumViewModel(
        albumRepository
    )
    MainScreen(viewModel)
}