package com.example.clockout.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.clockout.R
import com.example.clockout.ui.theme.ClockoutTheme
import org.threeten.bp.Duration
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter


// TODO: update this to mutable state when designs come
const val TOTAL_HRS = 7
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(modifier: Modifier = Modifier) {
    val clockInTime by remember { mutableStateOf(TimePickerState(7, 0, false)) }
    var lunchBreak by remember { mutableStateOf(30f) }
    // Recalculate lunchTime and clockOutTime whenever clockInTime or lunchBreak changes
    val lunchTime by remember {
        derivedStateOf { calculateLunchTime(clockInTime) }
    }
    val clockOutTime by remember {
        derivedStateOf { calculateClockOutTime(clockInTime, lunchBreak) }
    }

    val toggleState = remember {
        mutableStateOf(false)
    }
//
//    // Log values when clockInTime or lunchBreak changes
//    LaunchedEffect(  clockInTime, lunchBreak) {
//        Log.i("UNIQUETIME", "clockInTime changed to: $clockInTime")
//        Log.i("UNIQUETIME", "lunchBreak changed to: $lunchBreak")
//    }
//
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        TimePickerCard("Clock In", clockInTime, toggleState)
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
    Column {
        Text(text = title, Modifier.padding(all = 16.dp))
        Slider(
            value = sliderPosition,
            onValueChange = { newValue -> onvalueChange(newValue) },
            valueRange = 0f..60f,
            steps = 3,
            colors = SliderDefaults.colors(
                thumbColor = Color.White
            ),
            modifier = Modifier
                .semantics { contentDescription = "Lunch Break Slider" }
                .padding(horizontal = 16.dp),
        )
        Text(
            text = "${sliderPosition.toInt()} minutes",
            Modifier
                .align(Alignment.CenterHorizontally)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerCard(
    title: String,
    timePickerState: TimePickerState,
    toggleDisplayState: MutableState<Boolean>
) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding( all = 16.dp),

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
            if (toggleDisplayState.value) {
                TimePicker(
                    state = timePickerState,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally),
                )
            } else {
                TimeInput(
                    state = timePickerState,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally),
                )
            }
            IconButton(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(),
                onClick = {
                    toggleDisplayState.value = !toggleDisplayState.value
                    Log.i("PickerState", "Clock in time: ${calculateTime(timePickerState)} ")
                },
            ) {
                Icon(
                    painter = if (toggleDisplayState.value) {
                        painterResource(id = R.drawable.ic_keyboard)
                    } else {
                        painterResource(id = R.drawable.ic_time)
                    },
                    contentDescription = "Keyboard Icon",
                    modifier = Modifier.size(36.dp),
                )

            }
        }
    }
}


// Function to calculate time based on TimePickerState
@OptIn(ExperimentalMaterial3Api::class)
fun calculateTime(timePickerState: TimePickerState): String {
    // Replace this with your actual calculation logic
    return "${timePickerState.hour}:${timePickerState.minute}"
}

    @OptIn(ExperimentalMaterial3Api::class)
private fun calculateClockOutTime(clockInTime: TimePickerState, lunchBreak: Float): String {
    // Convert clockInTime to LocalTime
    val clockInLocalTime = LocalTime.of(clockInTime.hour, clockInTime.minute)

    // Calculate total work hours and lunch break in minutes
    val totalWorkHours = Duration.ofHours(TOTAL_HRS.toLong())
    val totalLunchBreak = Duration.ofMinutes(lunchBreak.toLong())

    // Calculate clock out time
    val clockOutLocalTime = clockInLocalTime.plus(totalWorkHours).plus(totalLunchBreak)

    // Format clockOutLocalTime into 12-hour format with AM/PM
    val formattedTime = DateTimeFormatter.ofPattern("h:mm a").format(clockOutLocalTime)

    return formattedTime
}


@OptIn(ExperimentalMaterial3Api::class)
private fun calculateLunchTime(clockInTime: TimePickerState): String {
    // Convert TimePickerState to LocalTime
    val clockInLocalTime = LocalTime.of(clockInTime.hour, clockInTime.minute)

    // Calculate lunch time (clockInTime + 5 hours)
    val lunchLocalTime = clockInLocalTime.plusHours(5)

    // Format lunch hour and minute
    val formattedLunchTime = lunchLocalTime.format(DateTimeFormatter.ofPattern("h:mm a"))

    return formattedLunchTime
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    ClockoutTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) { innerPadding ->
            App(modifier = Modifier.padding(innerPadding))
        }
    }

}
