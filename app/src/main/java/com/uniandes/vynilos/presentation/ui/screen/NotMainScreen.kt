package com.uniandes.vynilos.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.uniandes.vynilos.presentation.ui.theme.VynilOSTheme


@Composable
fun NotMainScreen(name: String = "Not Main Screen" ) {
    VynilOSTheme {
        Column {
            Text(name)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NotPreview() {
    NotMainScreen()
}