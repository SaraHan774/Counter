package com.august.counter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

@Composable
fun Home(
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.homeUiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = { viewModel.showDatePicker() }) {
            Text(text = "Set Start Date")
        }

        LazyColumn {
            itemsIndexed(items = uiState.daysAhead) { index, date ->
                Row {
                    Text(text = "Day ${(index + 1) * uiState.countUnit}")
                    Spacer(modifier = Modifier.size(24.dp))
                    Text(text = "${date.year}.${date.monthNumber}.${date.dayOfMonth}")
                }
            }
        }
    }

    if (uiState.isDatePickerVisible) {
        DatePickerView(
            onDismiss = remember { viewModel::hideDatePicker },
            onDateSelected = remember { viewModel::onDateSelected },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerView(
    onDismiss: () -> Unit = {},
    onDateSelected: (LocalDate) -> Unit = {},
) {
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()
        }
    })

    val selectedDate: LocalDate = datePickerState.selectedDateMillis?.let {
        Instant.fromEpochMilliseconds(it)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .toLocalDate()
    } ?: Counter.today()

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(selectedDate)
                onDismiss()
            }) { Text(text = "OK") }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) { Text(text = "Cancel") }
        }
    ) {
        DatePicker(
            state = datePickerState
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