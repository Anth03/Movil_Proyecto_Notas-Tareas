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
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditScreen(navController: NavHostController, viewModel: NoteViewModel, note: Note) {
    var title by remember { mutableStateOf(note.title) }
    var description by remember { mutableStateOf(note.description) }
    var classification by remember { mutableStateOf(note.classification) } // "NOTE" o "TASK"
    var dueDate by remember { mutableStateOf(note.dueDate ?: "") } // Fecha como String en formato DD/MM/AA
    var dueTime by remember { mutableStateOf(note.dueTime ?: "") } // Hora como String en formato HH:mm
    var isCompleted by remember { mutableStateOf(note.isCompleted) } // Solo para tareas

    val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.editar_nota), fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                actions = {
                    Text(
                        text = stringResource(id = R.string.guardar),
                        color = Color(0xff649562),
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                val updatedNote = note.copy(
                                    title = title,
                                    description = description,
                                    classification = classification,
                                    dueDate = if (classification == "TASK") dueDate else null,
                                    dueTime = if (classification == "TASK") dueTime else null,
                                    isCompleted = isCompleted
                                )
                                CoroutineScope(Dispatchers.IO).launch {
                                    viewModel.update(updatedNote)
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
            // Botones de grabar audio y abrir la cámara
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

            Spacer(modifier = Modifier.height(16.dp))

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
                    Text(text = stringResource(id = R.string.marcar_cumplida), modifier = Modifier.padding(start = 8.dp))
                }
            }

            // Botón para eliminar la nota/tarea
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.delete(note)
                        withContext(Dispatchers.Main) {
                            navController.navigateUp()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.eliminar), color = Color.White)
            }
        }
    }
}
