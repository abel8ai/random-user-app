package com.zerox.randomuserapp.ui.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zerox.randomuserapp.R
import com.zerox.randomuserapp.data.model.entities.user.User
import com.zerox.randomuserapp.databinding.ActivityMainBinding
import com.zerox.randomuserapp.ui.view.adapters.UserAdapter
import com.zerox.randomuserapp.ui.view_model.UserViewModel
import com.zerox.randomuserapp.ui.view_model.exceptions.FailedApiResponseException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var context: Context
    private var page = 1;
    private var userList = mutableListOf<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title = resources.getString(R.string.main_activity_title)
        context = this
        userViewModel
        loadData()
        userViewModel.usersModel.observe(this, Observer {
            userList = it
            initRecyclerView()
        })
    }

    private fun initRecyclerView() {
        binding.pbLoadingUsers.visibility = View.GONE
        binding.rvUsers.visibility = View.VISIBLE
        val manager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = manager
        adapter = UserAdapter(userList, page)
        binding.rvUsers.adapter = adapter
        binding.rvUsers.addOnScrolledToEnd {
            loadMoreData()
        }
    }

    private fun loadData() {
        binding.pbLoadingUsers.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                userViewModel.getAllUsers("?results=50&seed=abc&page=$page&inc=name,email,picture")
            } catch (exception: Exception) {

                runOnUiThread {
                    binding.pbLoadingUsers.visibility = View.GONE
                    Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun loadMoreData() {
        page++
        binding.pbLoadingUsers.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                userViewModel.getAllUsers("?results=50&seed=abc&page=$page&inc=name,email,picture")
            } catch (exception: Exception) {
                runOnUiThread {
                    Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun RecyclerView.addOnScrolledToEnd(onScrolledToEnd: () -> Unit) {

        this.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            private val VISIBLE_THRESHOLD = 5
            private var loading = true
            private var previousTotal = 0

            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int
            ) {

                with(layoutManager as LinearLayoutManager) {

                    val visibleItemCount = childCount
                    val totalItemCount = itemCount
                    val firstVisibleItem = findFirstVisibleItemPosition()

                    if (loading && totalItemCount > previousTotal) {

                        loading = false
                        previousTotal = totalItemCount
                    }

                    if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {

                        onScrolledToEnd()
                        loading = true
                    }
                }
            }
        })
    }
}