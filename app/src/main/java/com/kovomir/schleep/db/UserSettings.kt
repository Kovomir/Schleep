package com.kovomir.schleep.db

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

    @ColumnInfo(name = "targetSleepTime")
    var targetSleepTime: String? = "",

    // if the app is launched for the first time after installation
    @ColumnInfo(name = "firstLaunch")
    var firstLaunch: Boolean = true,

    @ColumnInfo(name = "userName")
    var userName: String = "Your name"
)
