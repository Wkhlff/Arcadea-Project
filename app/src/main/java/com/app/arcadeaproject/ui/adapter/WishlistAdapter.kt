package com.app.arcadeaproject.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.arcadeaproject.R
import com.app.arcadeaproject.data.local.LocalGame
import com.app.arcadeaproject.ui.main.DetailActivity
import com.bumptech.glide.Glide

class WishlistAdapter(
    private var games: List<LocalGame>,
    private val onRemoveClick: (LocalGame) -> Unit,
    private val onAddToCartClick: (LocalGame) -> Unit
) : RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder>() {

    class WishlistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivGame: ImageView = view.findViewById(R.id.iv_wishlist_img)
        val tvTitle: TextView = view.findViewById(R.id.tv_wishlist_title)
        val tvPrice: TextView = view.findViewById(R.id.tv_wishlist_price)
        val btnRemove: Button = view.findViewById(R.id.btn_wishlist_remove)
        val btnAddToCart: Button = view.findViewById(R.id.btn_wishlist_add_to_cart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wishlist_item, parent, false)
        return WishlistViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        val game = games[position]
        holder.tvTitle.text = game.title
        holder.tvPrice.text = game.price

        Glide.with(holder.itemView.context)
            .load(game.image)
            .placeholder(R.drawable.logo_arcadea)
            .into(holder.ivGame)

        holder.btnRemove.setOnClickListener { onRemoveClick(game) }
        holder.btnAddToCart.setOnClickListener { onAddToCartClick(game) }

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, DetailActivity::class.java).apply {
                putExtra("GAME_ID", game.id)
                putExtra("GAME_TITLE", game.title)
                putExtra("GAME_PRICE", game.price)
                putExtra("GAME_IMAGE_URL", game.image)
                putExtra("GAME_DESCRIPTION", game.description)
            }
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount() = games.size

    fun updateData(newGames: List<LocalGame>) {
        games = newGames
        notifyDataSetChanged()
    }
}
