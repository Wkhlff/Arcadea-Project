package com.app.arcadeaproject.data.remote.model

import com.google.gson.annotations.SerializedName

data class UserData(
    val id: Int?,
    val email: String?,
    @SerializedName("nama")
    val nama: String?,
    @SerializedName("foto_profile") // Ubah dari "gambar" ke "foto_profile" agar sesuai dengan upload
    val gambar: String?,
    @SerializedName("bio")
    val bio: String?
)
