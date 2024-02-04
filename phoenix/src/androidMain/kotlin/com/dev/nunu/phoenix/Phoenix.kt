package com.dev.nunu.phoenix

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import androidx.core.content.IntentCompat
import androidx.core.content.getSystemService

private const val KEY_RESTART_INTENTS = "phoenix_restart_intents"
private const val KEY_MAIN_PROCESS_PID = "phoenix_main_process_pid"

class Phoenix : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Process.killProcess(intent.getIntExtra(KEY_MAIN_PROCESS_PID, -1))

        val intents = IntentCompat.getParcelableArrayListExtra(
            intent,
            KEY_RESTART_INTENTS,
            Intent::class.java
        )
        startActivities(intents?.toTypedArray())
        finish()
        Runtime.getRuntime().exit(0)
    }

    companion object {
        @JvmStatic
        fun triggerRebirth(context: Context) {
            triggerRebirth(context, getRestartIntent(context))
        }

        @JvmStatic
        fun triggerRebirth(context: Context, vararg nextIntents: Intent) {
            if (nextIntents.isEmpty()) {
                throw IllegalArgumentException("Intents should not be empty")
            }

            nextIntents.first()
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

            val intent = Intent(context, Phoenix::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(KEY_RESTART_INTENTS, nextIntents)
                .putExtra(KEY_MAIN_PROCESS_PID, Process.myPid())

            context.startActivity(intent)
        }

        @JvmStatic
        private fun getRestartIntent(context: Context): Intent {
            val packageName = context.packageName
            val defaultIntent = context.packageManager.getLaunchIntentForPackage(packageName)
            if (defaultIntent != null) {
                return defaultIntent
            }
            throw IllegalStateException("Unable to determine default activity for $packageName. Does an activity specify the DEFAULT category in its intent filter?")
        }

        @JvmStatic
        fun isPhoenixProcess(context: Context): Boolean {
            val currentPid = Process.myPid()
            val activityManager = context.getSystemService<ActivityManager>()
            val runningAppProcesses = activityManager?.runningAppProcesses
            if (runningAppProcesses != null) {
                return runningAppProcesses.any {
                    it.pid == currentPid && it.processName.endsWith(":phoenix")
                }
            }
            return false
        }
    }
}
