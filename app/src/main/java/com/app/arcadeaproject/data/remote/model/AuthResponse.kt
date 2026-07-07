package com.app.arcadeaproject.data.remote.model

data class AuthResponse(
    val success: Boolean?,
    val message: String?,
    val token: String?,
    val user: UserData?
)
