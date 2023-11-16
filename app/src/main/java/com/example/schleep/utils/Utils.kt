package com.example.schleep.utils

import com.example.schleep.db.SleepRecord
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime


const val WELCOME_SCREEN_ROUTE: String = "welcomeScreen"
const val FIRST_SETUP_SCREEN_ROUTE: String = "firstSetup"
const val SIGNIN_SCREEN_ROUTE: String = "signInScreen"
const val LEADERBOARDS_ROUTE: String = "leaderboards"

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

/*fun toLocalTime(string: String?): LocalTime? {
    return string?.let { LocalTime.parse(it) }
}*/

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

fun getSleepRecordsAfterDate(
    sleepRecords: List<SleepRecord>,
    date: LocalDateTime
): List<SleepRecord> {
    val sleepRecordsAfterDate: ArrayList<SleepRecord> = ArrayList()
    if (sleepRecords.isEmpty()) {
        return sleepRecordsAfterDate
    }

    for (sleepRecord in sleepRecords) {
        if (toLocalDateTime(sleepRecord.endTime)!! >= date) {
            sleepRecordsAfterDate.add(sleepRecord)
        }
    }
    return sleepRecordsAfterDate
}

fun followedSleepRoutine(
    sleepRecord: SleepRecord,
    targetSleepDuration: Duration,
    targetSleepStartTime: LocalTime,
    targetWakeUpTime: LocalTime
): Boolean {
    val sleepDuration = getSleepLength(sleepRecord)

    val startTime = toLocalDateTime(sleepRecord.startTime)!!.toLocalTime()
    val endTime = toLocalDateTime(sleepRecord.endTime)!!.toLocalTime()

    //add 15 minute tolerance from both sides
    val startTimeWithinInterval =
        (startTime >= targetSleepStartTime.minusMinutes(15)
                && startTime <= targetSleepStartTime.plusMinutes(15))
    val endTimeWithinInterval =
        (endTime >= targetWakeUpTime.minusMinutes(15)
                && endTime <= targetWakeUpTime.plusMinutes(15))

    //add 30 minute offset because of corner cases with minimal startTime and maximal endTime
    val aboveMin = sleepDuration.compareTo(targetSleepDuration.minusMinutes(30)) != -1
    val underMax = sleepDuration.compareTo(targetSleepDuration.plusMinutes(30)) < 1
    val sleepDurationWithinInterval = aboveMin && underMax

    return (sleepDurationWithinInterval && startTimeWithinInterval && endTimeWithinInterval)

}

/*
fun isInternetAvailable(context: Context): Boolean {
    var result = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    result = when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }

    return result
}*/
