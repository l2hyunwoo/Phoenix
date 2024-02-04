package com.dev.nunu.phoenix

import android.content.Context
import android.content.Intent

actual interface Message

data class AndroidMessage(
    val context: Context,
    val intent: Intent? = null
) : Message

actual object PhoenixManager {
    actual fun triggerRebirth(message: Message) {
        if (message is AndroidMessage) {
            val (context, intent) = message
            if (intent == null) {
                Phoenix.triggerRebirth(context)
                return
            }
            Phoenix.triggerRebirth(context, intent)
        }
    }

    actual fun isPhoenixProcess(message: Message): Boolean {
        if (message is AndroidMessage) {
            return Phoenix.isPhoenixProcess(message.context)
        }
        return false
    }
}

