package com.example.schleep.components

import com.example.schleep.db.SleepRecord
import java.time.Duration
import java.time.LocalDateTime


fun getSleepLength(sleepRecord: SleepRecord): Duration {
    val sleepStart = toLocalDateTime(sleepRecord.startTime)
    val sleepEnd = toLocalDateTime(sleepRecord.endTime)
    return Duration.between(sleepStart, sleepEnd)
}

fun fromLocalDateTime(value: LocalDateTime?): String? {
    return value?.toString()
}

fun toLocalDateTime(value: String?): LocalDateTime? {
    return value?.let { LocalDateTime.parse(it) }
}