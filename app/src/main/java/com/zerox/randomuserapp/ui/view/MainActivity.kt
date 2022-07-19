package com.zerox.randomuserapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.zerox.randomuserapp.data.model.entities.user.User
import com.zerox.randomuserapp.databinding.ActivityMainBinding
import com.zerox.randomuserapp.ui.view.adapters.UserAdapter
import com.zerox.randomuserapp.ui.view_model.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val userViewModel : UserViewModel by viewModels()
    private lateinit var binding:ActivityMainBinding
    private lateinit var adapter : UserAdapter
    private var userList = mutableListOf<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userViewModel
        loadData()
        userViewModel.userModel.observe(this, Observer {
            userList = it
            initRecyclerView()
        })
    }

    private fun initRecyclerView() {
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter(userList)
        binding.rvUsers.adapter = adapter
    }
    private fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            userViewModel.getAllUsers()
        }
    }
}