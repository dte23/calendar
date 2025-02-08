package com.example.calendar

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalendarInstrumentationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun daysSinceJanuaryDialog_DisplaysCorrectDays() {
        // Enter month "1" into the month input field
        composeTestRule.onNodeWithTag("monthInput").performTextInput("1")

        // Enter year "2024" into the year input field
        composeTestRule.onNodeWithTag("yearInput").performTextInput("2024")

        // Click the "Show Calendar" button
        composeTestRule.onNodeWithTag("showCalendar").performClick()

        // Click on the day "5" in the calendar
        composeTestRule.onNodeWithTag("day_5").performClick()


        // Verify the dialog title is displayed
        composeTestRule.onNodeWithTag("daysPassedDialog").assertExists()


        // Verify the dialog message shows "4 days passed since January 1st"
        composeTestRule
            .onNodeWithTag("daysPassedDialog")
            .assertTextEquals("4 days have passed since january 1.")
    }
}