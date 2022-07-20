package com.zerox.randomuserapp.data.model.entities.user

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val info: Info,
    @SerializedName("results")
    val results: MutableList<User>
)