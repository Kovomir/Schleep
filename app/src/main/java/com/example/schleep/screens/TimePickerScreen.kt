package com.example.schleep.screens


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun TimePickerScreen(
    pickedTime: LocalTime,
    onTimePicked: (LocalTime) -> Unit,
    is24HourClock: Boolean = true,
    initialTime: LocalTime = LocalTime.now(),
    title: String = "Vyberte čas",
    minTime: LocalTime = LocalTime.MIN,
    maxTime: LocalTime = LocalTime.MAX
) {
    var pickedTime by remember {
        mutableStateOf(pickedTime)
    }
    val formattedTime by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("HH:mm")
                .format(pickedTime)
        }
    }

    val timeDialogState = rememberMaterialDialogState()
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.large
            ),
            onClick = { timeDialogState.show() },
        ) {
            Text(
                text = "Vyberte čas",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            text = formattedTime,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(all = 10.dp)
        )
    }

    MaterialDialog(
        dialogState = timeDialogState,
        buttons = {
            positiveButton(text = "Ok") {
                Toast.makeText(
                    context,
                    "Čas nastaven",
                    Toast.LENGTH_LONG
                ).show()
            }
            negativeButton(text = "Zrušit")
        }
    ) {
        timepicker(
            is24HourClock = is24HourClock,
            initialTime = initialTime,
            title = title,
            timeRange = minTime..maxTime,
            onTimeChange = {
                pickedTime = it
                onTimePicked(it)
            }
        )
    }
}