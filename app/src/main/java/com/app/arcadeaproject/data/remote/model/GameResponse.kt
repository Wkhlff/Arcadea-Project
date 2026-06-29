package com.app.arcadeaproject.data.remote.model

import com.google.gson.annotations.SerializedName

data class GameResponse(
    val id: Int,
    @SerializedName("nama")
    val judul: String,
    @SerializedName("harga")
    val harga: Int,
    @SerializedName("gambar")
    val gambar: String, // URL gambar
    @SerializedName("deskripsi")
    val deskripsi: String?, // Deskripsi game
    @SerializedName("persen_diskon")
    val persenDiskon: Int,
    @SerializedName("diskon_mulai")
    val diskonMulai: String?,
    @SerializedName("diskon_selesai")
    val diskonSelesai: String?
)
