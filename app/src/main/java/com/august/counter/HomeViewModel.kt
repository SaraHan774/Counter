package com.august.counter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate

class HomeViewModel : ViewModel() {
    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()

    private lateinit var counter: Counter

    fun onDateSelected(startDate: LocalDate) {
        counter = Counter(startDate)
        hideDatePicker()
        _homeUiState.update {
            it.copy(
                selectedDate = startDate,
                daysAhead = getDaysAhead(it.countUnit)
            )
        }
    }

    fun showDatePicker() {
        _homeUiState.update {
            it.copy(isDatePickerVisible = true)
        }
    }

    fun hideDatePicker() {
        _homeUiState.update {
            it.copy(isDatePickerVisible = false)
        }
    }

    private fun getDaysAhead(unit: Int): List<LocalDate> {
        val list = mutableListOf<LocalDate>()
        for (i in 1..10) {
            list.add(counter.count((i * unit).toLong()))
        }
        return list
    }
}