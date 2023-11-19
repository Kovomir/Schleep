package com.kovomir.schleep.db

import java.time.LocalTime

class UserSettingsRepository(private val userSettingsDao: UserSettingsDao) {

    fun getUserSettings(): UserSettings {
        var userSettings = userSettingsDao.getUserSettings()

        // Defaultní nastavení, pokud je databáze prázdná
        if (userSettings == null) {
            userSettings = UserSettings(
                id = 1,
                wakeUpTime = LocalTime.of(8, 0).toString(),
                targetSleepTime = LocalTime.of(8, 0).toString(),
                userName = "",
                firstLaunch = true
            )
            userSettingsDao.insertUserSettings(userSettings)
        }

        return userSettings
    }

    fun updateUserSettings(userSettings: UserSettings) {
        userSettingsDao.updateUserSettings(userSettings)
    }
}
