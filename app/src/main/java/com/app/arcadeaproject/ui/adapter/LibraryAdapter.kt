package com.app.arcadeaproject.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.arcadeaproject.R
import com.app.arcadeaproject.data.remote.model.GameResponse
import com.bumptech.glide.Glide

class LibraryAdapter(
    private var games: List<GameResponse>,
    private val onRefundClick: (GameResponse) -> Unit
) : RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder>() {

    class LibraryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivGame: ImageView = view.findViewById(R.id.iv_library_game_img)
        val tvTitle: TextView = view.findViewById(R.id.tv_library_game_title)
        val tvPlayedHours: TextView = view.findViewById(R.id.tv_library_played_hours)
        val tvLastPlayed: TextView = view.findViewById(R.id.tv_library_last_played)
        val btnPlay: Button = view.findViewById(R.id.btn_library_play)
        val btnRefund: Button = view.findViewById(R.id.btn_library_refund)
    }

    fun updateData(newGames: List<GameResponse>) {
        this.games = newGames
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.library_item_card, parent, false)
        return LibraryViewHolder(view)
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        val game = games[position]
        holder.tvTitle.text = game.judul

        holder.tvPlayedHours.text = "Ready to play"
        holder.tvLastPlayed.text = "Installed"

        Glide.with(holder.itemView.context)
            .load(game.gambar)
            .placeholder(R.drawable.logo_arcadea)
            .into(holder.ivGame)

        holder.btnPlay.setOnClickListener {
            Toast.makeText(it.context, "Launching ${game.judul}...", Toast.LENGTH_SHORT).show()
        }

        holder.btnRefund.setOnClickListener {
            onRefundClick(game)
        }
    }

    override fun getItemCount() = games.size
}
