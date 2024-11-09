package com.uniandes.vynilos.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.uniandes.vynilos.common.getSafeParcelableExtra
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.presentation.navigation.AlbumNavigation

class AlbumActivity : ComponentActivity() {

    companion object {
        const val ALBUM = "album"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ///se recibe el album por medio del intent
        val album = intent.getSafeParcelableExtra<Album>(ALBUM)
        if (album == null) finish()

        setContent {
            AlbumNavigation(album!!)
        }
    }
}