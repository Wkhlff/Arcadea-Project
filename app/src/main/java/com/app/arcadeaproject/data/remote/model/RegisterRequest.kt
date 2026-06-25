package com.app.arcadeaproject.data.remote.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("nama")
    val nama: String,
    val email: String,
    val password: String
)
