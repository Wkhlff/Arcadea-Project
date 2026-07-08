package com.app.arcadeaproject.data.remote.model

data class Post(
    val id: Int,
    val username: String,
    val gameName: String,
    val content: String,
    val imageResId: Int,
    val likes: Int,
    val comments: Int
)
