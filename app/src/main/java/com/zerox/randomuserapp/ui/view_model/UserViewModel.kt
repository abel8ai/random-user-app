package com.zerox.randomuserapp.ui.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zerox.randomuserapp.data.model.entities.user.User
import com.zerox.randomuserapp.data.model.entities.user.UserResponse
import com.zerox.randomuserapp.data.network.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userService: UserService
) : ViewModel() {

    val userModel = MutableLiveData<MutableList<User>>()

    suspend fun getAllUsers(){
        userModel.postValue(userService.getUsers("?result=50")!!.results)
    }
}