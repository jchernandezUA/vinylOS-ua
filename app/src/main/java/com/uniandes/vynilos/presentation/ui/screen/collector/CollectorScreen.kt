package com.uniandes.vynilos.presentation.ui.screen.collector

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.uniandes.vynilos.R
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Collector
import com.uniandes.vynilos.presentation.viewModel.ListCollectorViewModel

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun CollectorScreen(
    viewModel: ListCollectorViewModel
) {
    val collectorsResult by viewModel.collectorsResult.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCollectors()
    }

    Box(
        modifier = Modifier
            .fillMaxSize().padding(top = 64.dp),

        contentAlignment = Alignment.TopCenter
    ) {
        when (val result = collectorsResult) {
            is DataState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center)
                )
            }
            is DataState.Success -> {
                val data = result.data
                if (data.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(data) { collector ->
                            CollectorCard(collector)
                        }
                    }
                } else {
                    Text(
                        text = stringResource(R.string.no_collectors),
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            is DataState.Error -> {
                Text(
                    text = result.error.message,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {}
        }
    }
}

@Composable
fun CollectorCard(collector: Collector) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            val firstPerformerImage = collector.favoritePerformers.firstOrNull()?.image
            val painter = rememberAsyncImagePainter(firstPerformerImage)
            val imageModifier = Modifier
                .size(64.dp)
                .clip(CircleShape)

            if (painter.state is AsyncImagePainter.State.Success && firstPerformerImage != null) {
                Image(
                    painter = painter,
                    contentDescription = "Collector Image",
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Default Collector Icon",
                    modifier = imageModifier,
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(text = collector.name, fontWeight = FontWeight.Bold)
                Text(text = collector.email)
                Text(text = collector.telephone)
            }
        }
    }
}