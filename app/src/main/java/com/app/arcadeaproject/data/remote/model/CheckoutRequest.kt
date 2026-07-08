package com.app.arcadeaproject.data.remote.model

import com.google.gson.annotations.SerializedName

data class CheckoutRequest(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("games")
    val games: List<Int>
)
