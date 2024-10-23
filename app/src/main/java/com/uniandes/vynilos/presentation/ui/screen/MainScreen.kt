package com.uniandes.vynilos.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.uniandes.vynilos.common.DataStatus
import com.uniandes.vynilos.common.observeAsActions
import com.uniandes.vynilos.presentation.navigation.ActionType
import com.uniandes.vynilos.presentation.navigation.NavigationActions
import com.uniandes.vynilos.presentation.ui.theme.VynilOSTheme
import com.uniandes.vynilos.presentation.viewModel.AlbumViewModel


@Composable
fun MainScreen(
    viewModel: AlbumViewModel,
    navigationActions: NavigationActions = NavigationActions()
) {
    val albumsResult by viewModel.albumsResult.collectAsState()

    var errorMessage by remember { mutableStateOf("") }

    viewModel.albumsResult.observeAsActions {
        if (it?.status == DataStatus.ERROR) {
            errorMessage = it.error!!.message
        }
    }

    VynilOSTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

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

                when(albumsResult?.status) {
                    DataStatus.LOADING -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(100.dp)
                        )
                    }
                    DataStatus.SUCCESS -> {
                       albumsResult?.data?.forEach {
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
    val viewModel = AlbumViewModel()
    MainScreen(viewModel)
}