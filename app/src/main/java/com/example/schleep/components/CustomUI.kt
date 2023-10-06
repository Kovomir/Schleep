package com.example.schleep.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalLine(widthPercent: Int = 95, heightDp: Dp = 1.dp) {

    val widthFloat = widthPercent.toFloat() / 100

    Spacer(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(widthFloat)
            .height(height = heightDp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(size = 0.dp)
            )
    )
}