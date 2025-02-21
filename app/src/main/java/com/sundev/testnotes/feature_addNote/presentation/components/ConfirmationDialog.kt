package com.sundev.testnotes.feature_addNote.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.sundev.testnotes.ui.theme.TestNotesTheme

@Composable
fun ConfirmationDialog(
    modifier: Modifier = Modifier,
    dismissButton: () -> Unit,
    confirmButton: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { dismissButton() },
        title = {
            Text(
                text = "Alert", fontWeight = FontWeight.SemiBold, fontSize = 16.sp
            )
        },
        text = {
            Text(
                text = "Do you really want to delete this note?",
            )
        },
        dismissButton = {
            Button(onClick = { dismissButton() }) {
                Text(text = "No")
            }
        },
        confirmButton = {
            Button(
                onClick = { confirmButton() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(text = "Yes")
            }
        },
    )
}

@Preview
@Composable
fun ConfirmationDialogPreview() {

    TestNotesTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            ConfirmationDialog(
                dismissButton = {},
                confirmButton = {},
            )
        }
    }
}