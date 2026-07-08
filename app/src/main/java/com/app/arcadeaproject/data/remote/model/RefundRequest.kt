package com.app.arcadeaproject.data.remote.model

import com.google.gson.annotations.SerializedName

data class RefundRequest(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("game_id")
    val gameId: Int
)
