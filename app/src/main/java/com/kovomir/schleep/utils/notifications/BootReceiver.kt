package com.kovomir.schleep.utils.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    /*
    * restarts reminder on device startup
    * */
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            RemindersManager.startReminder(context)
        }
    }
}