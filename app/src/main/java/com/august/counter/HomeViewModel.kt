package com.august.counter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate

class HomeViewModel : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()

    fun onDateSelected(startDate: LocalDate) {
        hideDatePicker()
        _homeUiState.update {
            it.copy(
                selectedDate = startDate,
                daysAhead = getDaysAhead(it.countUnit)
            )
        }
    }

    private fun getDaysAhead(unit: Int): List<LocalDate> {
        val counter = Counter(homeUiState.value.selectedDate)
        val list = mutableListOf<LocalDate>()
        for (i in 1..10) {
            list.add(counter.count((i * unit).toLong()))
        }
        return list
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
}