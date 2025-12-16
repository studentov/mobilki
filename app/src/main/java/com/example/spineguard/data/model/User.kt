package com.example.spineguard.data.model

import java.util.Date

data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val height: Int? = null,
    val weight: Int? = null,
    val createdAt: Date = Date(),
    val settings: UserSettings = UserSettings()
)

data class UserSettings(
    val reminderEnabled: Boolean = true,
    val reminderInterval: Int = 60, // minutes
    val startTime: String = "08:00",
    val endTime: String = "22:00",
    val notificationsEnabled: Boolean = true
)