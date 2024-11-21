package com.uniandes.vynilos.presentation.navigation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uniandes.vynilos.R
import com.uniandes.vynilos.presentation.activities.AddAlbumActivity
import com.uniandes.vynilos.presentation.activities.AlbumActivity
import com.uniandes.vynilos.presentation.activities.ArtistActivity
import com.uniandes.vynilos.presentation.navigation.BottomNavItem.Companion.BOTTOM_ITEMS
import com.uniandes.vynilos.presentation.ui.screen.album.AlbumListScreen
import com.uniandes.vynilos.presentation.ui.screen.artist.ArtistScreen
import com.uniandes.vynilos.presentation.ui.screen.collector.CollectorScreen
import com.uniandes.vynilos.presentation.ui.theme.VynilOSTheme
import com.uniandes.vynilos.presentation.viewModel.album.ListAlbumViewModel
import com.uniandes.vynilos.presentation.viewModel.ListArtistViewModel
import com.uniandes.vynilos.presentation.viewModel.ListCollectorViewModel
import java.util.Locale


@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeNavigation(
    listArtistViewModel: ListArtistViewModel,
    listAlbumViewModel : ListAlbumViewModel,
    listCollectorViewModel: ListCollectorViewModel
) {

    val selectedTab = remember {
        mutableIntStateOf(1)
    }
    var isCollector by remember {
        mutableStateOf(false)
    }

    val navController = rememberNavController()
    VynilOSTheme {
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(color = Color.Black)
                        .padding(end = 15.dp)

                ) {
                    Text(
                        text = if(isCollector) {
                            stringResource(R.string.collector)
                        } else {
                            stringResource(R.string.visitor)
                        }.toUpperCase(Locale.getDefault()),
                        color =  MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.align(Alignment.CenterEnd)
                            .clickable {
                                isCollector = !isCollector
                            }
                    )
                }
            },
            bottomBar = {
            BottomBar(BOTTOM_ITEMS, selectedTab) { selectedItem ->
                changeScreen(navController, selectedItem)
            }
        }) { paddingValue ->
            NavHost(
                modifier = Modifier
                    .padding(bottom = 56.dp),
                navController = navController,
                startDestination = BottomNavItem.Albums.baseRoute
            ) {
                composable(BottomNavItem.Artists) {
                    val context = LocalContext.current

                    ArtistScreen(
                        viewModel = listArtistViewModel,
                        navigationActions = NavigationActions {
                            if (it is ArtistActions.OnClickArtist) {
                                val intent = Intent(context, ArtistActivity::class.java)
                                intent.putExtra(ArtistActivity.ARTIST, it.artist)
                                context.startActivity(intent)
                            }
                        }
                    )
                }
                composable(BottomNavItem.Albums) {
                    AlbumScreenWrapper(
                        modifier = Modifier.padding(
                            top = paddingValue.calculateTopPadding(),
                            bottom = 15.dp
                        ),
                        listAlbumViewModel,
                        isCollector
                    )

                }
                composable(BottomNavItem.Collectors.baseRoute) {
                    CollectorScreen(
                        viewModel = listCollectorViewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun AlbumScreenWrapper(
    modifier: Modifier = Modifier,
    listAlbumViewModel: ListAlbumViewModel,
    isCollector: Boolean = false
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){}
    AlbumListScreen(
        modifier = modifier,
        viewModel = listAlbumViewModel,
        isCollector = isCollector,
        navigationActions = NavigationActions {

            if (it is AlbumActions.OnClickAlbum) {
                val intent = Intent(context, AlbumActivity::class.java)
                intent.putExtra(AlbumActivity.ALBUM, it.album)
                context.startActivity(intent)
            }
            if (it is AlbumActions.OnClickAddAlbum)  {
                val intent = AddAlbumActivity.getIntent(context)
                launcher.launch(intent)
            }

        }
    )
}

private fun changeScreen(navController: NavController, navItem: BottomNavItem) {
    navController.navigate(navItem.baseRoute) {

        navController.graph.startDestinationRoute?.let { screenRoute ->
            popUpTo(screenRoute) {
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun BottomBar(
    items: List<BottomNavItem>,
    selectedTab: MutableState<Int>,
    onSelectedItem: (BottomNavItem) -> Unit
) {

    val selectedItem: Int by selectedTab

    Box {
        BottomAppBar (
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.Black,
        ) {
            val selectedColor = MaterialTheme.colorScheme.primary
            val unselectedColor = Color.White.copy(0.4f)
            for (i in items.indices) {
                val item = items[i]
                NavigationBarItem(
                    icon = {
                        Icon(
                            item.icon,
                            contentDescription = stringResource(id = item.title)
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = item.title),
                            color = if (items[selectedItem] == item)
                                selectedColor else
                                unselectedColor,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Black,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                    ),
                    alwaysShowLabel = true,
                    selected = items[selectedItem] == item,
                    onClick = {
                        selectedTab.value = i
                        onSelectedItem(item)
                    }
                )
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
fun BottomBar_Preview() {
    VynilOSTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
            Box(modifier = Modifier.fillMaxSize())
            BottomBar(
                items = listOf(BottomNavItem.Artists, BottomNavItem.Albums, BottomNavItem.Collectors),
                selectedTab = mutableIntStateOf(0), onSelectedItem = {}
            )
        }
    }
}
