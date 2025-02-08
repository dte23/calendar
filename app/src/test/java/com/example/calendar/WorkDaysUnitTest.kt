package com.example.calendar

import org.junit.Test

import org.junit.Assert.*

class WorkDaysUnitTest {
    @Test
    fun testWorkDaysCalculation() {
        val workDays = calculateWorkDays(month = 3, year = 2023)
        assertEquals("Workdays calculation failed", 23, workDays)
    }
}