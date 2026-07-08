package com.app.arcadeaproject.data.remote.model

import com.google.gson.annotations.SerializedName

data class MessageRequest(
    @SerializedName("sender_id") val senderId: Int,
    @SerializedName("receiver_id") val receiverId: Int,
    val message: String
)

data class MessageResponse(
    val success: Boolean,
    val messages: List<MessageData>?
)

data class MessageData(
    val id: Int,
    @SerializedName("sender_id") val senderId: Int,
    @SerializedName("receiver_id") val receiverId: Int,
    val message: String,
    @SerializedName("created_at") val createdAt: String
)

data class GeneralResponse(
    val success: Boolean,
    val message: String?
)
