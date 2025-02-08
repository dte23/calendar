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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.IsoFields

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalendarTheme {
                var showCalendar by rememberSaveable { mutableStateOf(false) }
                var month by rememberSaveable { mutableStateOf("") }
                var year by rememberSaveable { mutableStateOf("") }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BackgroundImage()
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        if (!showCalendar) {
                            GetInput(
                                onSubmit = { enteredMonth, enteredYear ->
                                    month = enteredMonth
                                    year = enteredYear
                                    showCalendar = true
                                }
                            )
                        } else {
                            DrawCalendar(
                                month = month.toInt(),
                                year = year.toInt()
                            ) {
                                showCalendar = false
                            }
                        }
                    }
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
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun GetInput(onSubmit: (String, String) -> Unit) {
    var month by rememberSaveable { mutableStateOf("") }
    var year by rememberSaveable { mutableStateOf("") }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    val invalidMonthError = stringResource(R.string.invalid_month_error)
    val invalidYearError = stringResource(R.string.invalid_year_error)
    val enterMonthLabel = stringResource(R.string.enter_month_label)
    val enterYearLabel = stringResource(R.string.enter_year_label)
    val showCalendarButton = stringResource(R.string.show_calendar_button)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            modifier = Modifier.background(color = Color.Black.copy(alpha = 0.3f)),
            value = month,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.toIntOrNull()?.let { it in 1..12 } == true) {
                    month = newValue
                    errorMessage = ""
                } else {
                    month = newValue
                    errorMessage = invalidMonthError
                }
            },
            label = { Text(enterMonthLabel) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            isError = errorMessage.isNotEmpty()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.background(color = Color.Black.copy(alpha = 0.3f)),
            value = year,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.toIntOrNull() != null) {
                    year = newValue
                    errorMessage = ""
                } else {
                    year = newValue
                    errorMessage = invalidYearError
                }
            },
            label = { Text(enterYearLabel) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            isError = errorMessage.isNotEmpty()
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.displaySmall
            )
        }

        Spacer(modifier = Modifier.height(160.dp))

        Button(
            onClick = { onSubmit(month, year) },
            enabled = month.isNotEmpty() && year.isNotEmpty() && errorMessage.isEmpty()
        ) {
            Text(showCalendarButton)
        }
    }
}

@Composable
fun DrawCalendar(month: Int, year: Int, onBack: () -> Unit) {
    val daysInMonth = YearMonth.of(year, month).lengthOfMonth()
    val firstDayOfMonth = (LocalDate.of(year, month, 1).dayOfWeek.value + 6) % 7
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var daysSinceStartOfYear by rememberSaveable { mutableIntStateOf(0) }

    val daysPassedDialogTitle = stringResource(R.string.days_passed_dialog_title)
    val daysPassedMessage = stringResource(R.string.days_passed_message)
    val weekdays = listOf(
        "",
        stringResource(R.string.monday),
        stringResource(R.string.tuesday),
        stringResource(R.string.wednesday),
        stringResource(R.string.thursday),
        stringResource(R.string.friday),
        stringResource(R.string.saturday),
        stringResource(R.string.sunday),
    )
    val months = listOf(
        stringResource(R.string.january),
        stringResource(R.string.february),
        stringResource(R.string.march),
        stringResource(R.string.april),
        stringResource(R.string.may),
        stringResource(R.string.june),
        stringResource(R.string.july),
        stringResource(R.string.august),
        stringResource(R.string.september),
        stringResource(R.string.october),
        stringResource(R.string.november),
        stringResource(R.string.december),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            Text(
                text = "${months[month - 1]} $year",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row {
            weekdays.forEach { day ->
                Box(
                    modifier = Modifier.weight(1f).aspectRatio(1f).border(1.dp, Color.Black)
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = day, fontWeight = FontWeight.Bold)
                }
            }
        }

        val totalCells = daysInMonth + firstDayOfMonth
        val rows = (totalCells / 7) + if (totalCells % 7 == 0) 0 else 1

        var dayCounter = 1

        Column {
            for (row in 0 until rows) {
                val weekNumber = LocalDate.of(year, month, (dayCounter).coerceAtMost(daysInMonth))
                    .get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)

                Row {
                    Box(
                        modifier = Modifier.weight(1f).aspectRatio(1f).border(1.dp, Color.Black)
                            .background(Color.Black.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = weekNumber.toString(), fontWeight = FontWeight.Bold)
                    }

                    for (col in 0 until 7) {
                        val index = row * 7 + col
                        Box(
                            modifier = Modifier.weight(1f).aspectRatio(1f).border(1.dp, Color.Black)
                                .background(if (index % 7 == 6) Color.Red.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (index >= firstDayOfMonth && dayCounter <= daysInMonth) {
                                val currentDay = dayCounter

                                Text(
                                    text = currentDay.toString(),
                                    modifier = Modifier.clickable {
                                        daysSinceStartOfYear = calculateDaysSinceJanuaryFirst(currentDay, month, year)
                                        showDialog = true
                                    }
                                )
                                dayCounter++
                            }
                        }
                    }
                }

            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.workdays_message, calculateWorkDays(month, year)),
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onBack) {
            Text("Back")
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(daysPassedDialogTitle) },
            text = { Text("$daysSinceStartOfYear $daysPassedMessage") },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

fun calculateWorkDays(month: Int, year: Int): Int {
    val daysInMonth = YearMonth.of(year, month).lengthOfMonth()
    // Finn antall tomme celler i første rad (avhengig av ukedagsrekkefølgen)
    val firstDayOfMonth = (LocalDate.of(year, month, 1).dayOfWeek.value + 6) % 7
    var workDays = 0
    var dayCounter = 1
    val totalCells = daysInMonth + firstDayOfMonth
    val rows = (totalCells / 7) + if (totalCells % 7 == 0) 0 else 1

    for (row in 0 until rows) {
        for (col in 0 until 7) {
            val index = row * 7 + col
            if (index >= firstDayOfMonth && dayCounter <= daysInMonth) {
                // Anta at col 5 og 6 (altså 0-basert: 5 og 6) er lørdag og søndag
                if (col != 5 && col != 6) {
                    workDays++
                }
                dayCounter++
            }
        }
    }
    return workDays
}

fun calculateDaysSinceJanuaryFirst(day: Int, month: Int, year: Int): Int {
    return LocalDate.of(year, month, day).dayOfYear - 1
}