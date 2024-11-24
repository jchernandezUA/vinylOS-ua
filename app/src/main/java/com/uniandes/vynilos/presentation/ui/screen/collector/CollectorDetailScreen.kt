package com.uniandes.vynilos.presentation.ui.screen.collector

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uniandes.vynilos.R
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Collector
import com.uniandes.vynilos.presentation.navigation.ActionType
import com.uniandes.vynilos.presentation.navigation.NavigationActions
import com.uniandes.vynilos.presentation.ui.theme.VynilOSTheme
import com.uniandes.vynilos.presentation.ui.theme.vynilOSTopAppBarColors
import com.uniandes.vynilos.presentation.viewModel.CollectorDetailViewModel

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun CollectorDetailScreen(
    viewModel: CollectorDetailViewModel,
    navigationActions: NavigationActions = NavigationActions()
){
    val collectorResult = viewModel.collectorResult.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCollector()
    }


    VynilOSTheme {
        Scaffold(
            topBar = {
                CollectorTopBar {
                    navigationActions.onAction(ActionType.OnBack)
                }
            }
        ){paddingValues ->
            Box(
                Modifier.fillMaxSize()
                .padding(paddingValues)
            ){
                when (val result = collectorResult.value){
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
                                onClick = { viewModel.getCollector() }
                            ) {
                                Text(text = stringResource(R.string.try_again))
                            }
                        }
                    }
                    is DataState.Success -> {
                        CollectorDetailView(result.data)
                    }
                    else -> {}
                }
            }

        }

    }

}


@Composable
private fun CollectorDetailView(collector : Collector){
    val ctx = LocalContext.current
    LazyColumn (
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        item{
            Column( modifier = Modifier
                .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = collector.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 1.dp)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CollectorTopBar(onBack: () -> Unit) {
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