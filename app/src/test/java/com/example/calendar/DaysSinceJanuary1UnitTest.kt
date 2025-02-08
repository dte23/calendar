package com.example.calendar

import org.junit.Assert.assertEquals
import org.junit.Test

class DaysSinceJanuary1UnitTest {
    @Test
    fun testDaysSinceJanuaryFirst() {
        // Checking the day after a leap day, so answer should be 31 + 29 = 60
        val daysSinceJan = calculateDaysSinceJanuaryFirst(day = 1, month = 3, year = 2024)
        assertEquals("Days passed calculation failed", 60, daysSinceJan)
    }
}