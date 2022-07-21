package com.zerox.randomuserapp.ui.view.adapters

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zerox.randomuserapp.data.model.entities.user.User
import com.zerox.randomuserapp.databinding.UserItemBinding
import com.zerox.randomuserapp.ui.view.UserDetailsActivity

class UserViewHolder(view:View):RecyclerView.ViewHolder(view){

    private val binding = UserItemBinding.bind(view)
    fun bind(user: User, page : Int){
        binding.tvUserName.text = user.name.title + ". " +user.name.first + " " +user.name.last
        binding.tvUserMail.text = user.email
        Picasso.get().load(user.picture.thumbnail).into(binding.ivUserImage)
        binding.cvElement.setOnClickListener{
            val intent = Intent(binding.root.context,UserDetailsActivity::class.java)
            intent.putExtra("user_email",user.email)
            intent.putExtra("user_page",page)
            binding.root.context.startActivity(intent)
        }
    }
}