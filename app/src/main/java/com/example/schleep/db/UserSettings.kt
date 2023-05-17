package com.example.schleep.db

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "userSettings")
data class UserSettings(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "wakeUpTime")
    var wakeUpTime: String? = "",

    @ColumnInfo(name = "value")
    var targetSleepTime: String? = ""
)
