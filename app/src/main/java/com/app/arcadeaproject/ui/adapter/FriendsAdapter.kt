package com.app.arcadeaproject.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.arcadeaproject.R
import com.app.arcadeaproject.data.remote.model.UserSearchData
import com.bumptech.glide.Glide

class FriendsAdapter(
    private var friends: List<UserSearchData>,
    private val onChatClick: (UserSearchData) -> Unit
) : RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivAvatar: ImageView = view.findViewById(R.id.iv_friend_avatar)
        val tvName: TextView = view.findViewById(R.id.tv_friend_name)
        val tvStatus: TextView = view.findViewById(R.id.tv_friend_status)
        val btnChat: ImageButton = view.findViewById(R.id.btn_chat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.social_item_friend, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = friends[position]
        holder.tvName.text = friend.nama
        holder.tvStatus.text = "Online" // Status logic can be improved later

        Glide.with(holder.itemView.context)
            .load(friend.fotoProfile)
            .placeholder(R.drawable.circle_avatar_bg)
            .circleCrop()
            .into(holder.ivAvatar)

        holder.btnChat.setOnClickListener {
            onChatClick(friend)
        }
    }

    override fun getItemCount(): Int = friends.size

    fun updateData(newFriends: List<UserSearchData>) {
        friends = newFriends
        notifyDataSetChanged()
    }
}