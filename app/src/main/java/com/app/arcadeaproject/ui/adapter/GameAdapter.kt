package com.app.arcadeaproject.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.arcadeaproject.R
import com.app.arcadeaproject.data.remote.model.GameResponse
import com.app.arcadeaproject.ui.main.DetailActivity
import com.bumptech.glide.Glide

class GameAdapter(private var games: List<GameResponse>) : RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivGame: ImageView = view.findViewById(R.id.iv_game_img)
        val tvTitle: TextView = view.findViewById(R.id.tv_game_title)
        val tvPrice: TextView = view.findViewById(R.id.tv_game_price)
    }

    fun updateData(newGames: List<GameResponse>) {
        this.games = newGames
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item_grid, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.tvTitle.text = game.judul
        holder.tvPrice.text = game.harga

        // Load gambar dari URL menggunakan Glide
        Glide.with(holder.itemView.context)
            .load(game.gambar)
            .placeholder(R.drawable.logo_arcadea)
            .into(holder.ivGame)

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra("GAME_TITLE", game.judul)
            intent.putExtra("GAME_PRICE", game.harga)
            intent.putExtra("GAME_IMAGE_URL", game.gambar)
            intent.putExtra("GAME_DESCRIPTION", game.deskripsi)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount() = games.size
}
