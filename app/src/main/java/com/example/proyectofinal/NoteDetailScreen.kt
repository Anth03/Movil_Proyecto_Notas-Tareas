package com.example.proyectofinal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.proyectofinal.data.Note
import kotlinx.coroutines.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(navController: NavHostController, viewModel: NoteViewModel) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var classification by remember { mutableStateOf("NOTE") } // "NOTE" o "TASK"
    var dueDate by remember { mutableStateOf("") } // Solo para tareas
    var dueTime by remember { mutableStateOf("") } // Solo para tareas
    var isCompleted by remember { mutableStateOf(false) } // Solo para tareas

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.anadir), fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                actions = {
                    Text(
                        text = stringResource(id = R.string.guardar),
                        color = Color(0xff649562),
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                val newNote = Note(
                                    title = title,
                                    description = description,
                                    classification = classification,
                                    dueDate = if (classification == "TASK") dueDate else null,
                                    dueTime = if (classification == "TASK") dueTime else null,
                                    isCompleted = isCompleted
                                )
                                CoroutineScope(Dispatchers.IO).launch {
                                    viewModel.insert(newNote)
                                    withContext(Dispatchers.Main) {
                                        navController.navigateUp()
                                    }
                                }
                            },
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    Text(
                        text = stringResource(id = R.string.cancelar),
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable { navController.navigateUp() },
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Radio Buttons para seleccionar si es una nota o tarea
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RadioButton(
                    selected = classification == "NOTE",
                    onClick = { classification = "NOTE" }
                )
                Text(text = stringResource(id = R.string.nota))
                RadioButton(
                    selected = classification == "TASK",
                    onClick = { classification = "TASK" }
                )
                Text(text = stringResource(id = R.string.tarea))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { /* Funcionalidad futura */ }) {
                    Text(text = stringResource(id = R.string.grabar_audio))
                }
                Button(onClick = { /* Funcionalidad futura */ }) {
                    Text(text = stringResource(id = R.string.abrir_camara))
                }
            }

            // Campos de texto para título y descripción
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(stringResource(id = R.string.nombre)) },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 4.dp)
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(id = R.string.descripcion)) },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 4.dp)
            )

            // Campos adicionales solo para tareas
            if (classification == "TASK") {
                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text(stringResource(id = R.string.fecha_cumplimiento)) },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(vertical = 4.dp)
                )
                OutlinedTextField(
                    value = dueTime,
                    onValueChange = { dueTime = it },
                    label = { Text(stringResource(id = R.string.hora_cumplimiento)) },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(vertical = 4.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isCompleted,
                        onCheckedChange = { isCompleted = it }
                    )
                    Text(text = "Marcar como cumplida", modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}
