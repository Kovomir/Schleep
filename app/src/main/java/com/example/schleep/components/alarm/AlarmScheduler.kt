package com.example.schleep.components.alarm

interface AlarmScheduler {
    fun schedule(item: AlarmItem)

    fun cancel(item: AlarmItem)
}