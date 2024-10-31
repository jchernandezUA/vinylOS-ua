package com.uniandes.vynilos.presentation.navigation

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.uniandes.vynilos.common.NetworkModule
import com.uniandes.vynilos.data.repository.AlbumRepository
import com.uniandes.vynilos.data.repository.AlbumRepositoryImpl
import com.uniandes.vynilos.presentation.MainActivity
import com.uniandes.vynilos.presentation.navigation.BottomNavItem.Companion.BOTTOM_ITEMS
import com.uniandes.vynilos.presentation.ui.screen.ArtistScreen
import com.uniandes.vynilos.presentation.ui.screen.NotMainScreen
import com.uniandes.vynilos.presentation.ui.theme.VynilOSTheme
import com.uniandes.vynilos.presentation.viewModel.ListArtistViewModel
import com.uniandes.vynilos.presentation.ui.screen.AlbumListScreen
import com.uniandes.vynilos.presentation.viewModel.ListAlbumViewModel


@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeNavigation(listArtistViewModel: ListArtistViewModel) {

    val selectedTab = remember {
        mutableIntStateOf(1)
    }

    val navController = rememberNavController()

    VynilOSTheme {
        Scaffold(bottomBar = {
            BottomBar(BOTTOM_ITEMS, selectedTab) { selectedItem ->
                changeScreen(navController, selectedItem)
            }
        }) { _ ->
            NavHost(
                modifier = Modifier
                    .padding(bottom = 56.dp),
                navController = navController,
                startDestination = BottomNavItem.Albums.baseRoute
            ) {
                composable(BottomNavItem.Artists) {
                    ArtistScreen(viewModel = listArtistViewModel)
                }
                composable(BottomNavItem.Albums) {
                    AlbumListScreen(viewModel = listAlbumViewModel)

                }
                composable(BottomNavItem.Collectors) {
                    NotMainScreen("collectors")
                }
            }
        }
    }
}

fun changeScreen(navController: NavController, navItem: BottomNavItem) {
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
        BottomNavigation(
            backgroundColor = MaterialTheme.colors.onBackground,
            modifier = Modifier.fillMaxWidth(),
        ) {
            val selectedColor = MaterialTheme.colors.primary
            val unselectedColor = Color.White.copy(0.4f)
            for (i in items.indices) {
                val item = items[i]
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = stringResource(id = item.title)
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = item.title),
                            color = if (items[selectedItem] == item)
                                selectedColor else
                                unselectedColor,
                            style = MaterialTheme.typography.caption
                        )
                    },
                    selectedContentColor = selectedColor,
                    unselectedContentColor = unselectedColor,
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
