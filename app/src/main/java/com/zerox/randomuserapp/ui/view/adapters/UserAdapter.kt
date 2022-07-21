package com.zerox.randomuserapp.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zerox.randomuserapp.R
import com.zerox.randomuserapp.data.model.entities.user.User

class UserAdapter(private val users: List<User>,private val page:Int):RecyclerView.Adapter<UserViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UserViewHolder(layoutInflater.inflate(R.layout.user_item, parent, false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = users[position]
        holder.bind(item, page)
    }

    override fun getItemCount(): Int = users.size
}