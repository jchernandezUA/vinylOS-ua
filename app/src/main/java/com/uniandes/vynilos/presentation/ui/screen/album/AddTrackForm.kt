package com.uniandes.vynilos.presentation.ui.screen.album

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uniandes.vynilos.R
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.presentation.viewModel.album.AlbumViewModel

@Composable
fun AddTrackForm(
    onTrackAdded: () -> Unit,
    onCancel: () -> Unit,
    viewModel: AlbumViewModel
) {
    var trackName by remember { mutableStateOf("") }
    var trackDuration by remember { mutableStateOf("") }
    val addTrackResult by viewModel.addTrackResult.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.add_new_track),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = trackName,
            onValueChange = { trackName = it },
            label = { Text(text = stringResource(R.string.track_name)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = trackDuration,
            onValueChange = { trackDuration = it },
            label = { Text(text = stringResource(R.string.track_duration)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = onCancel) {
                Text(text = stringResource(R.string.cancel))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    viewModel.addTrackToAlbum(
                        trackName = trackName,
                        trackDuration = trackDuration
                    )
                },
                enabled = trackName.isNotEmpty() && trackDuration.isNotEmpty()
            ) {
                Text(text = stringResource(R.string.add))
            }
        }

        LaunchedEffect(addTrackResult) {
            if (addTrackResult is DataState.Success) {
                trackName = ""
                trackDuration = ""
                viewModel.getAlbum()
                onTrackAdded()
                viewModel.resetAddTrackResult()
            }
        }

        when (addTrackResult) {
            is DataState.Loading -> {
                CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            }
            is DataState.Error -> {
                Text(
                    text = (addTrackResult as DataState.Error).error.message
                        ?: stringResource(R.string.error_adding_track),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            else -> {}
        }
    }
}
