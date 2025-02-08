package com.example.calendar

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
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

        composeTestRule.onNodeWithTag("monthInput").performTextInput("1")

        composeTestRule.onNodeWithTag("yearInput").performTextInput("2024")

        composeTestRule.onNodeWithTag("showCalendar").performClick()

        composeTestRule.onNodeWithTag("day_5").performClick()

        composeTestRule.onNodeWithTag("daysPassedDialog").assertExists()

        composeTestRule
            .onNodeWithTag("daysPassedDialog")
            .assertTextEquals("4 days have passed since january 1.")
    }
}