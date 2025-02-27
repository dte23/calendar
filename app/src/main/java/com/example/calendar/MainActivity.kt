
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.IsoFields
import androidx.compose.ui.platform.LocalConfiguration


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


// Date/Year input screen


@Composable
fun GetInput(onSubmit: (String, String) -> Unit) {
    var month by rememberSaveable { mutableStateOf("") }
    var year by rememberSaveable { mutableStateOf("") }
    var monthError by rememberSaveable { mutableStateOf("") }
    var yearError by rememberSaveable { mutableStateOf("") }

    val invalidMonthError = stringResource(R.string.invalid_month_error)
    val invalidYearError = stringResource(R.string.invalid_year_error)
    val enterMonthLabel = stringResource(R.string.enter_month_label)
    val enterYearLabel = stringResource(R.string.enter_year_label)
    val showCalendarButton = stringResource(R.string.show_calendar_button)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 'Box' surrounding the input fields/show calendar button
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .border(2.dp, Color.Black, shape = RoundedCornerShape(16.dp))
                .background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(16.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Month input field
                OutlinedTextField(
                    modifier = Modifier
                        .semantics { testTag = "monthInput" },
                    value = month,
                    onValueChange = { newValue ->
                        month = newValue
                        monthError = if (newValue.isNotEmpty() && newValue.toIntOrNull()?.let { it in 1..12 } != true) {
                            invalidMonthError
                        } else {
                            ""
                        }
                    },
                    label = { Text(enterMonthLabel) },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    isError = monthError.isNotEmpty()
                )

                if (monthError.isNotEmpty()) {
                    Text(
                        text = monthError,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Year input field
                OutlinedTextField(
                    modifier = Modifier
                        .semantics { testTag = "yearInput" },
                    value = year,
                    onValueChange = { newValue ->
                        year = newValue
                        yearError = if (newValue.isNotEmpty() && newValue.toIntOrNull() == null) {
                            invalidYearError
                        } else {
                            ""
                        }
                    },
                    label = { Text(enterYearLabel) },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    isError = yearError.isNotEmpty()
                )
                // Display year-specific error if any
                if (yearError.isNotEmpty()) {
                    Text(
                        text = yearError,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(160.dp))

                // Grey out button unless valid inputs
                Button(
                    onClick = { onSubmit(month, year) },
                    enabled = month.isNotEmpty() && year.isNotEmpty() && monthError.isEmpty() && yearError.isEmpty(),
                    modifier = Modifier.semantics { testTag = "showCalendar" }
                ) {
                    Text(showCalendarButton)
                }
            }
        }
    }
}


// Calendar screen


@Composable
fun DrawCalendar(month: Int, year: Int, onBack: () -> Unit) {
    // Date calc
    val daysInMonth = YearMonth.of(year, month).lengthOfMonth()
    //
    val firstDayOfMonth = (LocalDate.of(year, month, 1).dayOfWeek.value + 6) % 7
    //
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var daysSinceStartOfYear by rememberSaveable { mutableIntStateOf(0) }

    // String resources.
    val daysPassedDialogTitle = stringResource(R.string.days_passed_dialog_title)
    val daysPassedMessage = stringResource(R.string.days_passed_message)
    val weekdays = listOf(
        "", // This first cell is reserved for the week number.
        stringResource(R.string.monday),
        stringResource(R.string.tuesday),
        stringResource(R.string.wednesday),
        stringResource(R.string.thursday),
        stringResource(R.string.friday),
        stringResource(R.string.saturday),
        stringResource(R.string.sunday)
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
        stringResource(R.string.december)
    )

    // Calculate a fixed cell size.
    // We assume 16.dp padding on each side (total 32.dp) and 8 cells per row.
    val configuration = LocalConfiguration.current
    val totalHorizontalPadding = 32.dp
    val cellSize = (configuration.screenWidthDp.dp - totalHorizontalPadding) / 8

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Month/Year header as a long bar.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 0.6.dp)  // Band aid fixes for alignment problems
                .padding(end = 0.6.dp)
                .background(Color.Black.copy(alpha = 0.3f))
                .border(1.dp, Color.Black)
                .padding(vertical = 10.dp),   // Adjust vertical padding to taste.
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${months[month - 1]} $year",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White         // Use a contrasting color.
            )
        }

        // Weekdays header row.
        Row {
            weekdays.forEach { day ->
                Box(
                    modifier = Modifier
                        .size(cellSize)           // Use fixed size.
                        .border(1.dp, Color.Black)
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = day, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Calculate the total number of calendar cells and the number of rows.
        val totalCells = daysInMonth + firstDayOfMonth
        val rows = (totalCells / 7) + if (totalCells % 7 == 0) 0 else 1

        var dayCounter = 1

        // Calendar grid
        Column {
            for (row in 0 until rows) {
                // Determine the week number for the current row.
                val weekNumber = if (dayCounter <= daysInMonth) {
                    LocalDate.of(year, month, dayCounter).get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
                } else {
                    0
                }
                Row {
                    // Week number cell.
                        Box(
                            modifier = Modifier
                                .size(cellSize)
                                .border(1.dp, Color.Black)
                                .background(
                                    if (col == 6) Color.Red.copy(alpha = 0.3f)
                                    else Color.Black.copy(alpha = 0.3f)
                                )
                                .semantics { testTag = "day_$currentDay" },
                            contentAlignment = Alignment.Center
                        ) {
                            if (index >= firstDayOfMonth && dayCounter <= daysInMonth) {
                                val currentDay = dayCounter
                                Text(
                                    text = currentDay.toString(),
                                    modifier = Modifier.clickable {
                                        daysSinceStartOfYear =
                                            calculateDaysSinceJanuaryFirst(currentDay, month, year)
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



        // Display workdays message
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.3f))
                .border(1.dp, Color.Black)
                .padding(vertical = 10.dp)
                .padding(start = 0.6.dp)  // Band aid fixes for alignment problems
                .padding(end = 0.6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.workdays_message, calculateWorkDays(month, year)),
                fontSize = 18.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Back button.
        Button(onClick = onBack) {
            Text("Back")
        }
    }

    // Dialog to show days passed since Jan 1.
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = daysPassedDialogTitle)
            },
            text = {
                Text(
                    "$daysSinceStartOfYear $daysPassedMessage",
                    modifier = Modifier.semantics { testTag = "daysPassedDialog" }
                )
            },
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
    val firstDayOfMonth = (LocalDate.of(year, month, 1).dayOfWeek.value + 6) % 7
    var workDays = 0
    var dayCounter = 1
    val totalCells = daysInMonth + firstDayOfMonth
    val rows = (totalCells / 7) + if (totalCells % 7 == 0) 0 else 1

    for (row in 0 until rows) {
        for (col in 0 until 7) {
            val index = row * 7 + col
            if (index >= firstDayOfMonth && dayCounter <= daysInMonth) {
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
