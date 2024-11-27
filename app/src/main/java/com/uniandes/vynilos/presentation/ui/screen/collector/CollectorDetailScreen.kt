package com.uniandes.vynilos.presentation.ui.screen.collector

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.uniandes.vynilos.R
import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.data.model.Collector
import com.uniandes.vynilos.data.model.CollectorAlbum
import com.uniandes.vynilos.presentation.navigation.ActionType
import com.uniandes.vynilos.presentation.navigation.NavigationActions
import com.uniandes.vynilos.presentation.ui.screen.album.PerformerCard
import com.uniandes.vynilos.presentation.ui.theme.VynilOSTheme
import com.uniandes.vynilos.presentation.ui.theme.vynilOSTopAppBarColors
import com.uniandes.vynilos.presentation.viewModel.CollectorDetailViewModel

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun CollectorDetailScreen(
    viewModel: CollectorDetailViewModel,
    navigationActions: NavigationActions = NavigationActions()
){
    val collectorResult = viewModel.collectorState.collectAsState()

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
                    is DataState.Success<*> -> {
                        CollectorDetailView(result.data as Pair<Collector, List<CollectorAlbum>>)
                    }
                    else -> {}
                }
            }

        }

    }

}


@Composable
private fun CollectorDetailView(pair: Pair<Collector, List<CollectorAlbum>>){
    val ctx = LocalContext.current
    val collector = pair.first
    val listCollectorAlbum = pair.second
    LazyColumn (
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        // Album Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                contentAlignment = Center
            ) {
                val firstPerformerImage = collector.favoritePerformers.firstOrNull()?.image
                val painter = rememberAsyncImagePainter(firstPerformerImage)
                val imageModifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)

                if (painter.state is AsyncImagePainter.State.Success && firstPerformerImage != null) {
                    AsyncImage(
                        model = firstPerformerImage,
                        contentDescription = "Album Cover",
                        modifier = imageModifier,
                        contentScale = ContentScale.Crop
                    )
                }else{
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Default Collector Icon",
                        modifier = imageModifier,
                        tint = Color.Gray
                    )
                }
            }
        }

        item{
            Column( modifier = Modifier
                .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = collector.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }

        item {
            // Divider between the name and the next section
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Record Label and Genre
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = ctx.getString(R.string.Email)+":  ",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = collector.email,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = ctx.getString(R.string.telephone)+": ",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = collector.telephone,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

// Artistas preferidos Section
        item {
            Text(
                text = ctx.getString(R.string.favoritePerformers)+":",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (collector.favoritePerformers.isNotEmpty()) {
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    items(collector.favoritePerformers) { performer ->
                        PerformerCard(performer = performer)
                    }
                }
            }
        }else{
            item {
                Box(
                    Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.No_Performers_Collector),
                        color = Color.Gray,
                        modifier = Modifier.align(Center)
                    )
                }
            }
        }

        item {
            Text(
                text = ctx.getString(R.string.albums)+":  ",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 5.dp)
            )
        }


        if (listCollectorAlbum.isNotEmpty()) {
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    items(listCollectorAlbum) { collectorAlbum ->
                        AlbumsCardCollector(collectorAlbum = collectorAlbum)
                    }
                }
            }
        }else{
            item {
                Box(
                    Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.No_Album_Collector),
                        color = Color.Gray,
                        modifier = Modifier.align(Center)
                    )
                }
            }
        }


    }
}

@Composable
private fun AlbumsCardCollector(collectorAlbum: CollectorAlbum){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(
                width = 1.dp,
                color = Color(0xFF613EEA)
            ),
        colors = CardDefaults.cardColors(
            //containerColor = Color.LightGray
            containerColor = Color.Transparent
        )
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = rememberAsyncImagePainter(collectorAlbum.album!!.cover),
                contentDescription = stringResource(R.string.album_image),
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ){
                Row(
                    modifier = Modifier
                        .padding(5.dp)
                ){
                    Text(
                        text = collectorAlbum.album.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(5.dp)
                ){
                    Text(
                        text = stringResource(R.string.precio) + ":  ",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = collectorAlbum.price.toString() + "$",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                }

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