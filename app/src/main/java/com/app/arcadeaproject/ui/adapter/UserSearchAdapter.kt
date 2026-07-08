package com.app.arcadeaproject.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.arcadeaproject.R
import com.app.arcadeaproject.data.remote.model.UserSearchData
import com.bumptech.glide.Glide

class UserSearchAdapter(
    private var users: List<UserSearchData>,
    private var friendIds: Set<Int> = emptySet(),
    private val onAddFriendClick: (UserSearchData) -> Unit
) : RecyclerView.Adapter<UserSearchAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivAvatar: ImageView = view.findViewById(R.id.iv_user_avatar)
        val tvName: TextView = view.findViewById(R.id.tv_user_name)
        val btnAdd: Button = view.findViewById(R.id.btn_add_friend_action)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.social_item_user_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.tvName.text = user.nama

        Glide.with(holder.itemView.context)
            .load(user.fotoProfile)
            .placeholder(R.drawable.circle_avatar_bg)
            .circleCrop()
            .into(holder.ivAvatar)

        if (friendIds.contains(user.id)) {
            holder.btnAdd.text = "Friend"
            holder.btnAdd.setBackgroundColor(Color.GRAY)
            holder.btnAdd.isEnabled = false
        } else {
            holder.btnAdd.text = "Add"
            holder.btnAdd.setBackgroundColor(Color.parseColor("#A44AFF"))
            holder.btnAdd.isEnabled = true
            holder.btnAdd.setOnClickListener {
                onAddFriendClick(user)
            }
        }
    }

    override fun getItemCount(): Int = users.size

    fun updateData(newUsers: List<UserSearchData>, newFriendIds: Set<Int>) {
        users = newUsers
        friendIds = newFriendIds
        notifyDataSetChanged()
    }
}