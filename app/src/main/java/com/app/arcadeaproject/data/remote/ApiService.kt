package com.app.arcadeaproject.data.remote

import com.app.arcadeaproject.data.remote.model.AuthResponse
import com.app.arcadeaproject.data.remote.model.GameResponse
import com.app.arcadeaproject.data.remote.model.LoginRequest
import com.app.arcadeaproject.data.remote.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @GET("games")
    suspend fun getGames(): Response<List<GameResponse>>
}
