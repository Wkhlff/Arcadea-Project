package com.app.arcadeaproject.data.remote.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    val success: Boolean?,
    val message: String?,
    val token: String?,
    val user: UserData?
)

data class UserData(
    val id: Int?, // Di database int(11)
    val email: String?,
    @SerializedName("nama")
    val nama: String? // Di database kolom 'nama'
)
