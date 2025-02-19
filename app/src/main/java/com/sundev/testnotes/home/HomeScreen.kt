package com.sundev.testnotes.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.Gson
import com.sundev.testnotes.R
import com.sundev.testnotes.Routes
import com.sundev.testnotes.models.NoteModel
import com.sundev.testnotes.ui.theme.TestNotesTheme
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    navigateNext: (String) -> Unit,
) {

    val notes = viewModel.noteList

    LaunchedEffect(key1 = true,){
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is HomeViewModel.HomeEvent.NavigateNext -> navigateNext(event.route)
            }
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary)
                    .padding(start = 50.dp, top = 20.dp, bottom = 20.dp)
            ) {
                Text(
                    text = "Notes App",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val route = Routes.ADD_NOTE+"/-1"
                    navigateNext(route)
                },
                containerColor = MaterialTheme.colorScheme.tertiary
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_24),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.LightGray)
                .padding(it),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(
                vertical = 14.dp
            )
        ) {
            items(notes.size) { index ->
                val note = notes[index]
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { viewModel.listItemOnClick(note.id) }
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = note.title,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(text = note.description)

                }
            }
        }
    }


}

@Preview
@Composable
private fun HomeScreenPreview() {
    TestNotesTheme {
        Surface(
            color = Color.Gray,
//            modifier = Modifier.fillMaxSize()
        ) {
            HomeScreen(navigateNext = {})
        }
    }
}