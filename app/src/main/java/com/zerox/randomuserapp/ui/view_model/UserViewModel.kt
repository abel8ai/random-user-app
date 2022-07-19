package com.zerox.randomuserapp.ui.view_model

import androidx.lifecycle.ViewModel
import com.zerox.randomuserapp.data.network.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userService: UserService
) : ViewModel() {
}