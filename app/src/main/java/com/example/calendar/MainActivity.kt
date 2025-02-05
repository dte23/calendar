package com.example.calendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.calendar.ui.theme.CalendarTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalendarTheme {
                CalendarApp()
            }
        }
    }
}

@Composable
fun CalendarApp() {
    var showCalendar by rememberSaveable { mutableStateOf(false) }
    var month by rememberSaveable { mutableStateOf("") }
    var year by rememberSaveable { mutableStateOf("") }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        BackgroundImage()
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            if (!showCalendar) {
                GetInput(
                    onSubmit = { enteredMonth, enteredYear ->
                        month = enteredMonth
                        year = enteredYear
                        showCalendar = true
                    }
                )
            } else {
                DrawCalendar(month.toIntOrNull() ?: 1, year.toIntOrNull() ?: 2024) {
                    showCalendar = false // Go back when tapped
                }
            }
        }
    }
}

@Composable
fun BackgroundImage(modifier: Modifier = Modifier) {
    val image = painterResource(R.drawable.background_image)
    Image(
        painter = image,
        contentDescription = null,
        modifier = modifier.fillMaxSize(), // Ensure the image fills the available space
        contentScale = ContentScale.Crop // Crop the image to fill the space
    )
}

@Composable
fun GetInput(onSubmit: (String, String) -> Unit) {
    var month by rememberSaveable { mutableStateOf("") }
    var year by rememberSaveable { mutableStateOf("") }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = month,
            onValueChange = { newValue ->
                // Check if the new value is a valid number and within the range 1-12
                if (newValue.isEmpty() || newValue.toIntOrNull()?.let { it in 1..12 } == true) {
                    month = newValue
                    errorMessage = "" // Clear error message if input is valid
                } else {
                    errorMessage = "Please enter a valid month (1-12)"
                }
            },
            label = { Text("Enter Month (1-12)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            isError = errorMessage.isNotEmpty() // Indicate error state
        )


        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Enter Year") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red, // Change color as needed
                style = MaterialTheme.typography.displayMedium
            )
        }
        
        Spacer(modifier = Modifier.height(160.dp))

        Button(
            onClick = { onSubmit(month, year) },
            enabled = month.isNotEmpty() && year.isNotEmpty() && errorMessage.isEmpty()
        ) {
            Text("Show Calendar")
        }

    }
}

@Composable
fun DrawCalendar(month: Int, year: Int, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Calendar for $month/$year", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack) {
            Text("Back")
        }
    }
}
