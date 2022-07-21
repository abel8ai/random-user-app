package com.zerox.randomuserapp.data.network

import com.zerox.randomuserapp.data.model.entities.user.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.http.Url
import javax.inject.Inject

class UserService @Inject constructor(private val retrofit: Retrofit) {

    suspend fun getUsers(url:String):UserResponse?{
        return withContext(Dispatchers.IO){
            val response = retrofit.create(UserApiClient::class.java).getAllUsers(url)
            response.body()
        }
    }
}