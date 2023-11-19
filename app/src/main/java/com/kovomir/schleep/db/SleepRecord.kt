package com.kovomir.schleep.db

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sleepRecords")
data class SleepRecord(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "startTime")
    var startTime: String? = "",

    @ColumnInfo(name = "endTime")
    var endTime: String? = "",

    @ColumnInfo(name = "perceivedQuality")
    var perceivedQuality: String? = "",

    @ColumnInfo(name = "lateDinner")
    var lateDinner: Boolean = false,

    @ColumnInfo(name = "lateCoffee")
    var lateCoffee: Boolean = false,

    @ColumnInfo(name = "lateWorkOut")
    var lateWorkOut: Boolean = false,

    @ColumnInfo(name = "alcohol")
    var alcohol: Boolean = false,

    @ColumnInfo(name = "stress")
    var stress: Boolean = false
)
