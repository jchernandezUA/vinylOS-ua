package com.uniandes.vynilos.presentation.activities

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.uniandes.vynilos.common.getSafeSerializableExtra
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.presentation.navigation.AlbumNavigation

class AlbumActivity : ComponentActivity() {

    companion object {
        const val ALBUM = "album"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ///se recibe el album por medio del intent
        val album = intent.getSafeSerializableExtra<Album>(ALBUM).let {
            if (it == null) {
                finish()
            }
            return@let it
        }

        setContent {
            AlbumNavigation(album!!)
        }
    }
}