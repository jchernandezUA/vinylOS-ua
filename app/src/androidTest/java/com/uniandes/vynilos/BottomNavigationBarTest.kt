package com.uniandes.vynilos

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import com.uniandes.vynilos.presentation.navigation.HomeNavigation
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            HomeNavigation()
        }
    }

    private fun getString(@StringRes id: Int): String = composeTestRule.activity.getString(id)

    @Test
    fun testBottomBarDisplaysAllItems() {
        composeTestRule.onNodeWithText(getString(R.string.artists)).assertIsDisplayed()
        composeTestRule.onNodeWithText(getString(R.string.albums)).assertIsDisplayed()
        composeTestRule.onNodeWithText(getString(R.string.collectors)).assertIsDisplayed()
    }

   @Test
   fun testLoadArtistScreen() {
       composeTestRule.onNodeWithText(getString(R.string.artists)).performClick()
       composeTestRule.onNodeWithText("artists").assertIsDisplayed()
   }

    @Test
    fun testLoadAlbumsScreen() {
        composeTestRule.onNodeWithText(getString(R.string.albums)).performClick()
        composeTestRule.onNodeWithText("albums").assertIsDisplayed()
    }


    @Test
    fun testLoadCollectorScreen() {
        composeTestRule.onNodeWithText(getString(R.string.collectors)).performClick()
        composeTestRule.onNodeWithText("collectors").assertIsDisplayed()
    }
}
