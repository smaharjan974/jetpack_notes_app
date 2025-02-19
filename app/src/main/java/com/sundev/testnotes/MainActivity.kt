package com.sundev.testnotes

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.sundev.testnotes.addNote.AddNoteScreen
import com.sundev.testnotes.home.HomeScreen
import com.sundev.testnotes.ui.theme.TestNotesTheme

class MainActivity : ComponentActivity() {

    private val TAG = "MainActivity"

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestNotesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = Routes.HOME) {
                        composable(Routes.HOME) {

                            HomeScreen(
                                navigateNext = { route ->
                                    navController.navigate(route)
                                }
                            )
                        }

                        composable(
                            route = Routes.ADD_NOTE + "/{id}",
                            arguments = listOf(
                                navArgument("id"){
                                    this.type = NavType.IntType
                                    this.defaultValue = -1
                                }
                            )
                        ) {
                            AddNoteScreen(
                                navigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}
