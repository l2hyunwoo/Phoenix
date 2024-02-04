package com.dev.nunu.phoenix

actual interface Message


actual object PhoenixManager {
    actual fun triggerRebirth(message: Message) {
    }

    actual fun isPhoenixProcess(message: Message): Boolean {
        TODO("Not yet implemented")
    }
}