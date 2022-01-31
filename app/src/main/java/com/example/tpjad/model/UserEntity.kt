package com.example.tpjad.model

data class UserEntity(
        val id: String,
        val badgeNumber: String,
        val name: String,
        val role: UserType,
        val team: Team
)
