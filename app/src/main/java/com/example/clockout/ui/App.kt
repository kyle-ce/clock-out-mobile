package com.example.clockout.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.clockout.ui.theme.ClockoutTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(modifier: Modifier = Modifier) {
    val clockInTime by remember { mutableStateOf(TimePickerState(7, 0, false)) }
    var lunchBreak by remember { mutableStateOf(30f) }
    val lunchTime = calculateLunchTime(clockInTime)
    val clockOutTime = calculateClockOutTime(clockInTime, lunchBreak)

    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        TimePickerCard("Clock In", clockInTime)
        Card(
            modifier = Modifier.padding(horizontal = 16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                LunchSlider("Lunch Break", lunchBreak) { newValue -> lunchBreak = newValue }
            }
            Row(
                modifier = Modifier.padding(all = 16.dp)
            ) {
                OutlinedTextField(
                    value = lunchTime,
                    onValueChange = { /* Do nothing, this field is calculated */ },
                    label = { Text("Lunch") },
                    readOnly = true,
                    modifier = Modifier.weight(.5f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedTextField(
                    value = clockOutTime,
                    onValueChange = { /* Do nothing, this field is calculated */ },
                    label = { Text("Clockout") },
                    readOnly = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                    modifier = Modifier.weight(.5f)
                )
            }
        }


    }
}

@Composable
fun ClockOutTimeDisplay(clockOutTime: String) {
    Text(
        text = "You should clock out at: $clockOutTime",
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(16.dp)
//            .align(Alignment.CenterHorizontally)
    )
}

@Composable
private fun LunchSlider(title: String, sliderPosition: Float, onvalueChange: (Float) -> Unit) {
    var colors = SliderDefaults.colors(
        inactiveTrackColor = MaterialTheme.colorScheme.surface
    )
    Column {
        Text(text = title, Modifier.padding(all = 16.dp))
        Slider(value = sliderPosition,
            onValueChange = { newValue -> onvalueChange(newValue) },
            valueRange = 0f..60f,
            steps = 3,
            modifier = Modifier
                .semantics { contentDescription = "Lunch Break Slider" }
                .padding(horizontal = 16.dp),
            colors = colors)
        Text(
            text = "${sliderPosition.toInt()} minutes",
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(all = 8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerCard(title: String, timePickerState: TimePickerState) {
    var colors = TimePickerDefaults.colors(
        clockDialColor = MaterialTheme.colorScheme.surface,
        clockDialSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
        clockDialUnselectedContentColor = MaterialTheme.colorScheme.onSurface,
        selectorColor = MaterialTheme.colorScheme.primary,

        containerColor = Color.Blue,

        periodSelectorBorderColor = Color.Transparent,
        periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
        periodSelectorUnselectedContainerColor = Color.Transparent,
        periodSelectorSelectedContentColor = MaterialTheme.colorScheme.surface,
        periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurface,

// TODO: fix surface color tint to match clockDialColor
        timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = .9f),
        timeSelectorUnselectedContainerColor = Color.Transparent,
        timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onSurface,
        timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurface
    )

    Log.i("Clockface colors", "TimePickerCard: ${MaterialTheme.colorScheme.primary} ")
    Card(
        Modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp),

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                title,
                Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally)
            )
            TimePicker(
                state = timePickerState,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally),
                colors = colors
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun calculateClockOutTime(clockInTime: TimePickerState, lunchBreak: Float): String {
    // Implement your clock out calculation logic here
    val clockInHour = clockInTime.hour
    val clockInMinute = clockInTime.minute
    val totalMinutes = (clockInHour * 60 + clockInMinute + (8 * 60) + lunchBreak.toInt())
    val clockOutHour = totalMinutes / 60
    val clockOutMinute = totalMinutes % 60
    return "${clockOutHour % 12}:${
        clockOutMinute.toString().padStart(2, '0')
    } ${if (clockOutHour >= 12) "PM" else "AM"}"
}

@OptIn(ExperimentalMaterial3Api::class)
private fun calculateLunchTime(clockInTime: TimePickerState): String {
    val lunchHour = if (clockInTime.hour + 5 == 12) 12 else (clockInTime.hour + 5) % 12
    val isPM = (clockInTime.hour + 5 >= 12)
    val lunchMinute = clockInTime.minute
    return "${lunchHour}:${lunchMinute.toString().padStart(2, '0')} ${if (isPM) "PM" else "AM"}"
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    ClockoutTheme(darkTheme = !false) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Blue)
        ) { innerPadding ->
            App(modifier = Modifier.padding(innerPadding))
        }
    }

}
