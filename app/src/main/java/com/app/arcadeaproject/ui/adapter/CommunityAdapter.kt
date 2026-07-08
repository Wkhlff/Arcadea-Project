package com.app.arcadeaproject.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.arcadeaproject.R
import com.app.arcadeaproject.data.remote.model.Post

class CommunityAdapter(private var posts: List<Post>) :
    RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder>() {

    fun updateData(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_community_post, parent, false)
        return CommunityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size

    class CommunityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvUsername: TextView = itemView.findViewById(R.id.tv_username)
        private val tvPostedIn: TextView = itemView.findViewById(R.id.tv_posted_in)
        private val tvContent: TextView = itemView.findViewById(R.id.tv_content)
        private val ivPostImage: ImageView = itemView.findViewById(R.id.iv_post_image)
        private val tvLikes: TextView = itemView.findViewById(R.id.tv_likes)
        private val tvComments: TextView = itemView.findViewById(R.id.tv_comments)

        fun bind(post: Post) {
            tvUsername.text = post.username
            tvPostedIn.text = "posted in ${post.gameName}"
            tvContent.text = post.content
            ivPostImage.setImageResource(post.imageResId)
            tvLikes.text = post.likes.toString()
            tvComments.text = post.comments.toString()
        }
    }
}
