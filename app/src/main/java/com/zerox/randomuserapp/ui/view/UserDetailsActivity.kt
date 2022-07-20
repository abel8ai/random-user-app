package com.zerox.randomuserapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zerox.randomuserapp.databinding.ActivityUserDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}