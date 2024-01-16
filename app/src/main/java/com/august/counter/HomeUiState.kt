package com.august.counter

import kotlinx.datetime.LocalDate

data class HomeUiState(
    val countUnit: Int = 100,
    val selectedDate: LocalDate = Counter.today(),
    val daysAhead: List<LocalDate> = emptyList(),
    val isDatePickerVisible: Boolean = false
)