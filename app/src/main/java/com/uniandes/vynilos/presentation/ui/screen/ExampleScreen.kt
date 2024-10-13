package com.uniandes.vynilos.presentation.ui.screen

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uniandes.vynilos.common.DataStatus
import com.uniandes.vynilos.common.observeAsActions
import com.uniandes.vynilos.presentation.ui.theme.VynilOSTheme
import com.uniandes.vynilos.presentation.viewModel.ExampleViewModel
import kotlinx.coroutines.flow.onEach


@Composable
fun ExampleScreen(viewModel: ExampleViewModel) {
    val userResult by viewModel.greeting.collectAsState()

    val userId by viewModel.userId.collectAsState()

    var errorMessage by remember { mutableStateOf("") }

    viewModel.greeting.observeAsActions {
        if (it?.status == DataStatus.ERROR) {
            errorMessage = it.error!!.message
        }
    }

    VynilOSTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

            Column {
                TextField(
                    value = userId,
                    onValueChange = {
                        viewModel.setUserId(it)
                    }
                )
                Button(
                    onClick = { viewModel.getUser() }
                ) {
                    Text(text = "Get User")
                }

                when(userResult?.status) {
                    DataStatus.LOADING -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(100.dp)
                        )
                    }
                    DataStatus.SUCCESS -> {
                        GreetingMessage(
                            name = viewModel.getFullName(),
                            modifier = Modifier.padding(innerPadding)
                        )
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

@Composable
fun GreetingMessage(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val viewModel = ExampleViewModel()
    ExampleScreen(viewModel)
}