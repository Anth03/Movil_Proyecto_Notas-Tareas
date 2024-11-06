package com.example.proyectofinal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyectofinal.viewmodel.NoteViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.Dp
import com.example.proyectofinal.data.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(navController: NavHostController, viewModel: NoteViewModel) {
    val notes by viewModel.allNotes.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.titulo),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .background(Color(0xFF4CAF50), shape = RoundedCornerShape(12.dp))
                            .padding(8.dp)
                    )
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFF4CAF50), shape = CircleShape)
                            .clickable {
                                navController.navigate("noteDetail")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+",
                            color = Color.White,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBar()
            Spacer(modifier = Modifier.height(9.dp))
            NoteGrid(notes = notes, navController)
        }
    }
}

@Composable
fun SearchBar() {
    var searchQuery by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text(text = stringResource(id = R.string.buscar), color = Color.White) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(id = R.string.buscar),
                tint = Color.White
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.Black, shape = RoundedCornerShape(10.dp))
    )
}

@Composable
fun NoteItem(
    note: Note,
    navController: NavHostController,
    width: Dp = 250.dp,
    height: Dp = 200.dp
) {
    // Determina el color de fondo según la clasificación y el estado de la tarea
    val backgroundColor = when {
        note.classification == "NOTE" -> Color(0xff8dec91) // Verde para notas
        note.isCompleted -> Color(0xff9fc7f1) // Azul claro para tareas
        else -> Color(0xff9fc7f1) // Azul para tareas
    }

    Box(
        modifier = Modifier
            .size(width, height)
            .padding(5.dp)
            .background(backgroundColor, shape = RoundedCornerShape(20.dp))
            .clickable {
                navController.navigate("editNote/${note.id}")
            },
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(7.dp)
        ) {


            Text(
                text = note.title ?: stringResource(id = R.string.tarea),
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = Color.Black
            )
            // Título (Nota o Tarea)
            Text(
                text = if (note.classification == "NOTE") {
                    stringResource(id = R.string.nota)
                } else {
                    stringResource(id = R.string.tarea)
                },
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xff606161)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Descripción de la nota/tarea
            Text(
                text = note.description,
                fontSize = 20.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Mostrar fechas si es una tarea
            if (note.classification == "TASK" && !note.dueDate.isNullOrEmpty() && !note.dueTime.isNullOrEmpty()) {
                Text(
                    text = "${note.dueDate} - ${note.dueTime}",
                    fontSize = 15.sp,
                    color = Color(0xff606161)
                )
            }

            // Mostrar "Terminada" si la tarea está completada
            if (note.isCompleted) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = R.string.terminada),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00796B) // Verde oscuro para el texto de "Terminada"
                )
            }
        }
    }
}

@Composable
fun NoteGrid(notes: List<Note>, navController: NavHostController) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 180.dp),
        modifier = Modifier.padding(8.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(notes) { note ->
            NoteItem(note = note, navController = navController)
        }
    }
}
