package com.august.counter

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime


class Counter(
    private val startDate: LocalDate
) {
    fun count(days: Long): LocalDate {
        return startDate.plus(days, DateTimeUnit.DAY)
    }

    companion object {
        fun today(): LocalDate {
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            return today.toLocalDate()
        }
    }
}

fun LocalDateTime.toLocalDate(): LocalDate {
    return LocalDate(year, monthNumber, dayOfMonth)
}