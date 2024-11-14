package com.uniandes.vynilos.presentation.ui.screen.collector

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.presentation.viewModel.CollectorDetailViewModel

@Composable
fun CollectorDetailScreen(
    collectorId: Int,
    collectorDetailViewModel: CollectorDetailViewModel
) {
    val collectorResult by collectorDetailViewModel.collectorResult.collectAsState()

    Scaffold { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            when (val result = collectorResult) {
                is DataState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is DataState.Success -> {
                    val collector = result.data
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = collector.name,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Email: ${collector.email}")
                        Text(text = "Teléfono: ${collector.telephone}")
                    }
                }
                is DataState.Error -> {
                    Text(
                        text = result.error.message ?: "Error desconocido",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {}
            }
        }
    }
}
