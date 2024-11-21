package com.uniandes.vynilos.presentation.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.uniandes.vynilos.presentation.navigation.NavigationActions
import com.uniandes.vynilos.presentation.ui.screen.album.AddAlbumScreen
import com.uniandes.vynilos.presentation.viewModel.album.AddAlbumViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddAlbumActivity : ComponentActivity() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, AddAlbumActivity::class.java)
        }
    }

    private val viewModel: AddAlbumViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AddAlbumScreen(
                viewModel = viewModel,
                navigationActions = NavigationActions {
                    setResult(RESULT_OK, Intent())
                    finish()
                }
            )
        }
    }
}