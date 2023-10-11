package com.example.schleep.components

import com.example.schleep.db.SleepRecord
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime

const val WELCOME_SCREEN_ROUTE: String = "welcomeScreen"
const val FIRST_SETUP_SCREEN_ROUTE: String = "firstSetup"

fun getSleepLength(sleepRecord: SleepRecord): Duration {
    val sleepStart = toLocalDateTime(sleepRecord.startTime)
    val sleepEnd = toLocalDateTime(sleepRecord.endTime)
    return Duration.between(sleepStart, sleepEnd)
}

fun fromLocalDateTime(date: LocalDateTime?): String? {
    return date?.toString()
}

fun toLocalDateTime(string: String?): LocalDateTime? {
    return string?.let { LocalDateTime.parse(it) }
}

val dayOfWeekToStringMap =
    mapOf(1 to "Po", 2 to "Út", 3 to "St", 4 to "Čt", 5 to "Pá", 6 to "So", 7 to "Ne")

fun getDayOfWeekAsString(date: LocalDateTime?): String? {
    return dayOfWeekToStringMap[date?.dayOfWeek?.value]
}

fun getSleepRecordLenght(sleepRecord: SleepRecord): String {
    val lenghtInMinutes = getSleepLength(sleepRecord).toMinutes().toInt()
    val hours = lenghtInMinutes / 60
    val minutes = lenghtInMinutes % 60
    return "$hours h $minutes m"
}

fun countBedTime(wakeUpTime: LocalTime, targetSleepTime: LocalTime): LocalTime {
    return wakeUpTime.minusHours(targetSleepTime.hour.toLong())
        .minusMinutes(targetSleepTime.minute.toLong())
}