package com.zerox.randomuserapp.ui.view_model


import android.os.Build.VERSION_CODES.Q

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.zerox.randomuserapp.data.model.entities.user.User
import com.zerox.randomuserapp.data.network.UserService
import com.zerox.randomuserapp.ui.view_model.exceptions.EmailNotFoundException
import com.zerox.randomuserapp.ui.view_model.exceptions.FailedApiResponseException
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Q], manifest = "src/main/AndroidManifest.xml", packageName = "com.zerox.randomuserapp")
internal class UserViewModelTest{

    @MockK
    private lateinit var userService: UserService
    private lateinit var userViewModel: UserViewModel
    private val url = "?results=50&seed=abc&page=1&inc=name,email,picture,location"
    private val email = "laura.woods@example.com"

    @Before
    fun initialize(){
        MockKAnnotations.init(this)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://randomuser.me/api/1.4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        userService = UserService(retrofit)
        userViewModel = UserViewModel(userService)
        userService = mockk()
    }
    @Test
    fun shouldReturnAListOf50UsersWithNameEmailPictureAndLocation() = runBlocking {
        // given
        val userResponse = userService.getUsers(url)!!
        coEvery { userService.getUsers(url)} returns userResponse
        // when
        userViewModel.getAllUsers(url)
        val result = userViewModel.usersModel.getorAwaitValue()
        // then
        coVerify(exactly = 1) { userService.getUsers(url) }
        Truth.assertThat(result == userResponse.results).isTrue()
    }
    @Test
    fun shouldThrowFailedApiResponseExceptionOnNullResponse(): Unit = runBlocking {
        // given
        coEvery { userService.getUsers(url) }returns null
        // when
        userViewModel.getAllUsers(url)
        // then
        coVerify(exactly = 1) { userService.getUsers(url) }
        Assert.assertThrows(FailedApiResponseException::class.java){
            runBlocking { userViewModel.getAllUsers(url) }
        }

    }
    @Test
    fun shouldReturnTheUserForTheGivenEmail() = runBlocking {
        // given
        val userResponse = userService.getUsers(url)!!
        val userList = userResponse.results
        var postUser: User? = null
        for (user in userList) {
            if (user.email == email)
                postUser = user
        }
        coEvery { userService.getUsers(url) }returns userResponse

        // when
        userViewModel.getUserByEmail(url,email)
        val result = userViewModel.userModel.getorAwaitValue()
        // then
        coVerify(exactly = 1) { userService.getUsers(url) }
        Truth.assertThat(result == postUser).isTrue()
    }
    @Test
    fun shouldThrowEmailNotFoundExceptionWhenTheGivenEmailDoesntExists(): Unit = runBlocking {
        // given
        coEvery { userService.getUsers(url) }returns null
        // when
        userViewModel.getUserByEmail(url,"qwe")
        // then
        coVerify(exactly = 1) { userService.getUsers(url) }
        Assert.assertThrows(EmailNotFoundException::class.java){
            runBlocking { userViewModel.getUserByEmail(url,"qwe") }
        }
    }
}