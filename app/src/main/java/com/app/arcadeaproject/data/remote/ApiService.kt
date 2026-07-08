package com.app.arcadeaproject.data.remote

import com.app.arcadeaproject.data.remote.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

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

    // --- Friend Routes ---
    @GET("friends/search")
    suspend fun searchUser(@Query("nama") nama: String): Response<SearchUserResponse>

    @POST("friends")
    suspend fun addFriend(@Body request: Map<String, Int>): Response<GeneralResponse>

    @GET("friends/{id}")
    suspend fun getFriends(@Path("id") userId: Int): Response<SearchUserResponse>

    // --- Message Routes ---
    @POST("messages")
    suspend fun sendMessage(@Body request: MessageRequest): Response<GeneralResponse>

    @GET("messages/{sender}/{receiver}")
    suspend fun getMessages(
        @Path("sender") senderId: Int,
        @Path("receiver") receiverId: Int
    ): Response<MessageResponse>
}
