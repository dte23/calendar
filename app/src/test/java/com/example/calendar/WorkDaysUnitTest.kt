package com.example.calendar

import org.junit.Test

import org.junit.Assert.*

class WorkDaysUnitTest {
    @Test
    fun testWorkDaysCalculation() {
        val workDays = calculateWorkDays(month = 3, year = 2023)
        // La oss si at vi forventer 23 arbeidsdager i mars 2023.
        assertEquals("Arbeidsdager i mars 2023", 23, workDays)
    }
}