package com.august.counter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.august.counter.ui.theme.CounterTheme
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CounterTheme {
                Home()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.homeUiState.collectAsState()

    Column {
        Button(onClick = { viewModel.showDatePicker() }) {
            Text(text = "Set Start Date")
        }

        LazyColumn {
            itemsIndexed(uiState.daysAhead) {index, date->
                Row {
                    Text(text = "Day ${(index + 1) * uiState.countUnit}")
                    Spacer(modifier = Modifier.size(24.dp))
                    Text(text = "${date.year}.${date.monthNumber}.${date.dayOfMonth}")
                }
            }
        }
    }

    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()
        }
    })

    // FIXME : 초기값이 today 로 나오는 문제 수정
    val selectedDate: LocalDate = datePickerState.selectedDateMillis?.let {
        Instant.fromEpochMilliseconds(it)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .toLocalDate()
    } ?: Counter.today()

    if (uiState.isDatePickerVisible) {
        Dialog(
            onDismissRequest = {
                viewModel.hideDatePicker()
            }
        ) {
            Column(
                modifier = Modifier.background(Color.White)
            ) {
                DatePickerView(
                    state = datePickerState,
                )
                Button(onClick = {
                    viewModel.onDateSelected(selectedDate)
                }) {
                    Text(text = "Confirm")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerView(
    state: DatePickerState,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        DatePicker(
            state = state,
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CounterTheme {
        Home()
    }
}