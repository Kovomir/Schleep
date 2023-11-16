package com.example.schleep.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalLine(widthPercent: Int = 95, heightDp: Dp = 1.dp, color: Color = MaterialTheme.colorScheme.primary) {

    val widthFloat = widthPercent.toFloat() / 100
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        Spacer(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(widthFloat)
                .height(height = heightDp)
                .background(
                    color = color,
                    shape = RoundedCornerShape(size = 0.dp)
                )
        )
    }
}
