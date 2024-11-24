package com.uniandes.vynilos.presentation.ui.screen.album

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uniandes.vynilos.R
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.common.ObserveAsActions
import com.uniandes.vynilos.common.fixTimeOffset
import com.uniandes.vynilos.presentation.navigation.ActionType
import com.uniandes.vynilos.presentation.navigation.AlbumActions
import com.uniandes.vynilos.presentation.navigation.NavigationActions
import com.uniandes.vynilos.presentation.ui.component.DropdownTextField
import com.uniandes.vynilos.presentation.ui.component.ProgressDialog
import com.uniandes.vynilos.presentation.ui.preview.PreviewViewModel
import com.uniandes.vynilos.presentation.ui.theme.VynilOSTheme
import com.uniandes.vynilos.presentation.ui.theme.vynilOSTopAppBarColors
import com.uniandes.vynilos.presentation.viewModel.album.AddAlbumViewModel
import com.uniandes.vynilos.presentation.ui.component.TextField
import java.util.Date


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AddAlbumScreen(
    viewModel: AddAlbumViewModel,
    navigationActions: NavigationActions = NavigationActions()
) {
    val albumsResult by viewModel.albumResult.collectAsState()

    val isValidForm by viewModel.isValidForm.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val genres = context.resources.getStringArray(R.array.genres)
    val recordLabels = context.resources.getStringArray(R.array.record_labels)
    val album by viewModel.name.collectAsState()
    val description by viewModel.description.collectAsState()
    val cover by viewModel.cover.collectAsState()
    val releaseDate by viewModel.releaseDate.collectAsState()
    val recordLabel by viewModel.recordLabel.collectAsState()
    val genre by viewModel.genre.collectAsState()
    val albumErrorId by viewModel.albumErrorId.collectAsState()
    val coverErrorId by viewModel.coverErrorId.collectAsState()
    val recordLabelErrorId by viewModel.recordLabelErrorId.collectAsState()
    val genreErrorId by viewModel.genreErrorId.collectAsState()
    val descriptionErrorId by viewModel.descriptionErrorId.collectAsState()
    val releaseDateErrorId by viewModel.releaseDateErrorId.collectAsState()


    viewModel.albumResult.ObserveAsActions {
        if (it is DataState.Success) {
            navigationActions.onAction(AlbumActions.OnAlbumAdded)
        }
    }

    if (showDatePicker) {
        DatePickerView(
            onDismiss = {
                showDatePicker = false
                viewModel.validateReleaseDate()
            },
            onDateSelected = {
                showDatePicker = false
                viewModel.onReleaseDateChange(it)
            }
        )
    }

    ProgressDialog(albumsResult is DataState.Loading)

    VynilOSTheme {
        Box {
            Scaffold(
                topBar = { AddAlbumTopBar {
                    navigationActions.onAction(ActionType.OnBack)
                } }
            ) { padding ->
                Column(
                    Modifier
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
                        .padding(bottom = 50.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = album,
                        onValueChange = { viewModel.onNameChange(it) },
                        label = stringResource(R.string.album),
                        textError = albumErrorId?.let { stringResource(it) }
                    )
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = description,
                        onValueChange = { viewModel.onDescriptionChange(it) },
                        label = stringResource(R.string.description),
                        textError = descriptionErrorId?.let { stringResource(it) }

                    )

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = cover,
                        onValueChange = { viewModel.onCoverChange(it) },
                        label = stringResource(R.string.image),
                        textError = coverErrorId?.let { stringResource(it) }
                    )
                    Column {
                        androidx.compose.material3.TextField(
                            modifier = Modifier
                                .fillMaxWidth().clickable {
                                    showDatePicker = true
                                },
                            value = releaseDate,
                            onValueChange = {},
                            isError = releaseDateErrorId != null,
                            label = { Text(stringResource(R.string.release_date)) },
                            trailingIcon = {
                                IconButton(onClick = {
                                    showDatePicker = true
                                }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                }
                            },
                            interactionSource = remember { MutableInteractionSource() }
                                .also { interactionSource ->
                                    LaunchedEffect(interactionSource) {
                                        interactionSource.interactions.collect {
                                            if (it is PressInteraction.Press) {
                                                showDatePicker = true
                                            }

                                        }
                                    }
                                }
                        )
                        if (releaseDateErrorId != null) {
                            Text(
                                text = stringResource(releaseDateErrorId!!),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    DropdownTextField(
                        genre,
                        stringResource(R.string.genre),
                        genres.toList(),
                        onItemSelected = {
                            viewModel.onGenreChange(it)
                        },
                        textError = genreErrorId?.let { stringResource(it) }
                    )

                    DropdownTextField(
                        recordLabel,
                        stringResource(R.string.record_label),
                        recordLabels.toList(),
                        onItemSelected = {
                            viewModel.onRecordLabelChange(it)
                        },
                        textError = recordLabelErrorId?.let { stringResource(it) }
                    )
                }
            }
            if (isValidForm) {
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    onClick = viewModel::addAlbum,
                ) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = stringResource(R.string.add_album)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerView(
    onDateSelected: (date: Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = Date().time)

    DatePickerDialog(
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(
                    fixTimeOffset(datePickerState.selectedDateMillis!!)
                )
            }) {
                Text(stringResource(R.string.select))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddAlbumTopBar(onBack: () -> Unit) {
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

@Preview(showBackground = true)
@Composable
fun AddAlbumScreenPreview() {
    AddAlbumScreen(
        PreviewViewModel.getAddAlbumViewModel()
    )
}