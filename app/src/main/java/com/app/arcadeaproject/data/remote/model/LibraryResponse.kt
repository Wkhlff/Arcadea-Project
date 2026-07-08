package com.app.arcadeaproject.data.remote.model

data class LibraryResponse(
    val success: Boolean,
    val games: List<GameResponse>
)
