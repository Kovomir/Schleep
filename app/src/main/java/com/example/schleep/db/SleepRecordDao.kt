package com.example.schleep.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface SleepRecordDao {

    @Query("SELECT * FROM sleepRecords")
    fun getAll(): List<SleepRecord>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSleepRecord(sleepRecord: SleepRecord)

    @Query("SELECT * FROM sleepRecords WHERE id = :id")
    fun getSleepRecordById(id: Int): SleepRecord

    @Query("SELECT * FROM sleepRecords ORDER BY startTime DESC LIMIT 1")
    fun getLastSleepRecord(): SleepRecord

    @Query("SELECT * FROM sleepRecords ORDER BY startTime DESC")
    fun getAllSleepRecords(): List<SleepRecord>

    @Query("SELECT * FROM sleepRecords WHERE (startTime != \"\" AND endTime != \"\") ORDER BY startTime DESC")
    fun getAllCompleteSleepRecords(): List<SleepRecord>

    @Query("DELETE FROM sleepRecords WHERE id = :id")
    fun deleteSleepRecordById(id: Int)

    @Update
    fun updateSleepRecord(SleepRecord:SleepRecord)
}