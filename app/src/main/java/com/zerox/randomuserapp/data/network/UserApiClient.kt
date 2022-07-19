package com.zerox.randomuserapp.data.network

import com.zerox.randomuserapp.data.model.entities.user.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface UserApiClient {
    @GET("/.json")
    suspend fun getAllUsers(@Url url: String):Response<UserResponse>
}