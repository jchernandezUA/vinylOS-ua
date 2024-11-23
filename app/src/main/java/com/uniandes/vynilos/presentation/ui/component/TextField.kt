package com.uniandes.vynilos.presentation.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    textError: String? = null,
    readOnly: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    var touched by remember { mutableStateOf(false) }

    Column {
        androidx.compose.material3.TextField(
            modifier = modifier
                .fillMaxWidth()
                .onFocusEvent {
                    if (it.isFocused) {
                        touched = true
                    } else if (touched){
                        onValueChange(value)
                    }
                },
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            label = { Text(label) },
            isError = touched && textError != null,
            readOnly = readOnly,
            interactionSource = interactionSource,
            trailingIcon = trailingIcon
        )
        if (textError != null) {
            Text(
                text = textError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}