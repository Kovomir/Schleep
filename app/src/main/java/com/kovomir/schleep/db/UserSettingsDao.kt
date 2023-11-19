package com.kovomir.schleep.db
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserSettingsDao {
    @Query("SELECT * FROM userSettings WHERE id = 1")
    fun getUserSettings(): UserSettings

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUserSettings(userSettings: UserSettings)

    @Update
    fun updateUserSettings(userSettings: UserSettings)
}