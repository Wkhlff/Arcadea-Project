package com.app.arcadeaproject.data.remote.model

import com.google.gson.annotations.SerializedName

data class GameResponse(
    val id: Int,
    @SerializedName("nama")
    val judul: String,
    @SerializedName("harga")
    val harga: String,
    @SerializedName("gambar")
    val gambar: String, // URL gambar
    @SerializedName("deskripsi")
    val deskripsi: String // Deskripsi game
)
