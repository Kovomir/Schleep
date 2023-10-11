package com.example.schleep.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SleepRecord::class, UserSettings::class], version = 4)
@TypeConverters(LocalDateTimeConverter::class)
abstract class SchleepDatabase : RoomDatabase() {

    abstract val sleepRecordDao: SleepRecordDao

    abstract val userSettingsDao: UserSettingsDao

    companion object {

        @Volatile
        private var INSTANCE: SchleepDatabase? = null

        fun getDatabase(context: Context): SchleepDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, SchleepDatabase::class.java, "schleep_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}