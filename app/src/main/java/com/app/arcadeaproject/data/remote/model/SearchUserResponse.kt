package com.app.arcadeaproject.data.remote.model

import com.google.gson.annotations.SerializedName

data class SearchUserResponse(
    val success: Boolean,
    val users: List<UserSearchData>?,
    val friends: List<UserSearchData>? // Added for getFriends endpoint
)

data class UserSearchData(
    val id: Int,
    val nama: String,
    @SerializedName("foto_profile")
    val fotoProfile: String?
)
