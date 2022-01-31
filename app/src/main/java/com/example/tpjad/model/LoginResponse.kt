package com.example.tpjad.model

data class LoginResponse(
    val token: String,
    val user: UserEntity
)