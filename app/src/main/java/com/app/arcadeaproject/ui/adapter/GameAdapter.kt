package com.app.arcadeaproject.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.arcadeaproject.R
import com.app.arcadeaproject.ui.main.DetailActivity

data class Game(val title: String, val price: String, val imageResId: Int)

class GameAdapter(private val games: List<Game>) : RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivGame: ImageView = view.findViewById(R.id.iv_game_img)
        val tvTitle: TextView = view.findViewById(R.id.tv_game_title)
        val tvPrice: TextView = view.findViewById(R.id.tv_game_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game_grid, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.tvTitle.text = game.title
        holder.tvPrice.text = game.price
        holder.ivGame.setImageResource(game.imageResId)

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra("GAME_TITLE", game.title)
            intent.putExtra("GAME_PRICE", game.price)
            intent.putExtra("GAME_IMAGE", game.imageResId)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount() = games.size
}