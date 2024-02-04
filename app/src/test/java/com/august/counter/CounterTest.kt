package com.august.counter

import kotlinx.datetime.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class CounterTest {

    @Test
    fun `given 10 days_when startDate is 2024-01-01_then returns 2024-01-11`() {
        val startDate = LocalDate(2024, 1, 1)
        val expected = Counter(startDate).count(10)
        assertEquals(expected, LocalDate(2024, 1, 11))
        //TODO : Hello World!
    }
}