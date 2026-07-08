package com.app.arcadeaproject.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.arcadeaproject.R
import com.app.arcadeaproject.data.local.LocalGame
import com.bumptech.glide.Glide

class CartAdapter(
    private var games: List<LocalGame>,
    private val onRemoveClick: (LocalGame) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivGame: ImageView = view.findViewById(R.id.iv_cart_game_img)
        val tvTitle: TextView = view.findViewById(R.id.tv_cart_game_title)
        val tvPrice: TextView = view.findViewById(R.id.tv_cart_game_price)
        val tvRemove: TextView = view.findViewById(R.id.tv_cart_remove)
    }

    fun updateData(newGames: List<LocalGame>) {
        this.games = newGames
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val game = games[position]
        holder.tvTitle.text = game.title
        holder.tvPrice.text = game.price

        Glide.with(holder.itemView.context)
            .load(game.image)
            .placeholder(R.drawable.logo_arcadea)
            .into(holder.ivGame)

        holder.tvRemove.setOnClickListener {
            onRemoveClick(game)
        }
    }

    override fun getItemCount() = games.size
}
