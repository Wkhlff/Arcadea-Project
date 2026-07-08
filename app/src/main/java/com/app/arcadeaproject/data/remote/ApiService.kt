package com.app.arcadeaproject.data.remote

import com.app.arcadeaproject.data.remote.model.AuthResponse
import com.app.arcadeaproject.data.remote.model.CheckoutRequest
import com.app.arcadeaproject.data.remote.model.CheckoutResponse
import com.app.arcadeaproject.data.remote.model.GameResponse
import com.app.arcadeaproject.data.remote.model.LibraryResponse
import com.app.arcadeaproject.data.remote.model.LoginRequest
import com.app.arcadeaproject.data.remote.model.RegisterRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @GET("games")
    suspend fun getGames(): Response<List<GameResponse>>

    @GET("profile/{id}")
    suspend fun getProfile(@Path("id") id: Int): Response<AuthResponse>

    @Multipart
    @PUT("profile/{id}")
    suspend fun updateProfile(
        @Path("id") id: Int,
        @Part("nama") nama: RequestBody,
        @Part("email") email: RequestBody,
        @Part("bio") bio: RequestBody,
        @Part foto_profile: MultipartBody.Part?
    ): Response<AuthResponse>

    @POST("checkout")
    suspend fun checkout(@Body request: CheckoutRequest): Response<CheckoutResponse>

    @GET("library/{id}")
    suspend fun getLibrary(@Path("id") userId: Int): Response<LibraryResponse>
}
