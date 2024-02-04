package com.dev.nunu.phoenix

expect interface Message

expect object PhoenixManager {
    fun triggerRebirth(message: Message)
    fun isPhoenixProcess(message: Message): Boolean
}
