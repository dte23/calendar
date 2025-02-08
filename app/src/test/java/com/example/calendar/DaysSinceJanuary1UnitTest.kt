package com.example.calendar

import org.junit.Assert.assertEquals
import org.junit.Test

class DaysSinceJanuary1UnitTest {
    @Test
    fun testDaysSinceJanuaryFirst() {
        // Eksempel: 15. februar 2023.
        // For 15. februar 2023 er dayOfYear = 31 (januar) + 15 = 46,
        // men funksjonen returnerer 46 - 1 = 45.
        val daysSinceJan = calculateDaysSinceJanuaryFirst(day = 15, month = 2, year = 2023)
        assertEquals("Dager siden 1. januar for 15. februar 2023", 45, daysSinceJan)
    }
}