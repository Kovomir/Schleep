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
)
