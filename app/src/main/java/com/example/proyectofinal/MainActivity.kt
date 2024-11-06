package com.example.proyectofinal

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinal.ui.theme.ContactAppTheme
import com.example.proyectofinal.viewmodel.NoteViewModel
import java.util.*

class MainActivity : ComponentActivity() {

    private val viewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultLocale(this)
        setContent {
            ContactAppTheme {
                MainContent(viewModel)
            }
        }
    }

    private fun setDefaultLocale(context: Context) {
        val defaultLanguage = "es"
        val currentLanguage = Locale.getDefault().language

        if (currentLanguage != "es" && currentLanguage != "en") {
            val locale = Locale(defaultLanguage)
            Locale.setDefault(locale)
            val config = Configuration()
            config.setLocale(locale)
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
        }
    }
}

@Composable
fun MainContent(viewModel: NoteViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        AppNavigation(
            navController = navController,
            viewModel = viewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
