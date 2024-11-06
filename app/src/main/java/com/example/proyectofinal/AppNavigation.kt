package com.example.proyectofinal

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinal.viewmodel.NoteViewModel
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    viewModel: NoteViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = "noteList", modifier = modifier) {
        composable("noteList") {
            NoteListScreen(navController = navController, viewModel = viewModel)
        }
        composable("noteDetail") {
            NoteDetailScreen(navController = navController, viewModel = viewModel)
        }
        composable("editNote/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull()
            val note = noteId?.let { viewModel.getNoteById(it).observeAsState().value }
            if (note != null) {
                NoteEditScreen(navController = navController, viewModel = viewModel, note = note)
            }
        }
    }
}
