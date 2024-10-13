package com.uniandes.vynilos.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.collect

@Composable
fun <T> Flow<T>.observeAsActions(onEach: (T) -> Unit) {
    val flow = this
    LaunchedEffect(key1 = flow) {
        flow.onEach(onEach).collect()
    }
}