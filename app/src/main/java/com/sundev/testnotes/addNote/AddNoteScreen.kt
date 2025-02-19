package com.sundev.testnotes.addNote

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sundev.testnotes.R
import com.sundev.testnotes.models.NoteModel
import com.sundev.testnotes.ui.theme.TestNotesTheme
import kotlinx.coroutines.flow.collectLatest

@ExperimentalMaterial3Api
@Composable
fun AddNoteScreen(
    viewModel: AddNoteViewModel = viewModel(),
    navigateBack: () -> Unit
) {

    val title = viewModel.title.collectAsState()
    val description = viewModel.description.collectAsState()

    LaunchedEffect(key1 = true, block = {
        viewModel.event.collectLatest { event ->
            when (event) {
                is AddNoteViewModel.Event.NavigateBack -> navigateBack()
            }
        }
    }
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // AppBar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .clickable { viewModel.backIconOnClick() },
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = null,
                tint = Color.White
            )
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.baseline_delete_24),
                contentDescription = null,
                tint = Color.White
            )
        }

        TextField(
            value = title.value,
            onValueChange = { viewModel.titleOnValueChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Enter title")
            },
            textStyle = LocalTextStyle.current.copy(
                fontSize = 24.sp, fontWeight = FontWeight.Bold
            )
        )

        TextField(
            value = description.value, onValueChange = { viewModel.descriptionOnValueChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            placeholder = {
                Text(text = "Enter Description")
            },
        )


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AddNotePreview() {
    TestNotesTheme {
        Surface {
            AddNoteScreen(
                navigateBack = {
//                    Log.d(TAG, "AddNotePreview: ")
                }
            )
        }
    }
}
