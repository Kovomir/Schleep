package com.kovomir.schleep.db

class SleepRecordRepository(private val sleepRecordDao: SleepRecordDao) {

    fun insertSleepRecord(sleepRecord: SleepRecord) {
        sleepRecordDao.insertSleepRecord(sleepRecord)
    }

    fun getSleepRecordById(id: Int): SleepRecord {
        return sleepRecordDao.getSleepRecordById(id)
    }

    fun getAllSleepRecords(): List<SleepRecord> {
        return sleepRecordDao.getAllSleepRecords()
    }

    fun getAllCompleteSleepRecords(): List<SleepRecord> {
        return sleepRecordDao.getAllCompleteSleepRecords()
    }

    fun getLastSleepRecord(): SleepRecord {
        if (getAllSleepRecords().isEmpty()) {
            return SleepRecord()
        }
        return sleepRecordDao.getLastSleepRecord()
    }

    fun deleteSleepRecordById(id: Int) {
        sleepRecordDao.deleteSleepRecordById(id)
    }

    fun updateSleepRecord(sleepRecord: SleepRecord) {
        sleepRecordDao.updateSleepRecord(sleepRecord)
    }
}
