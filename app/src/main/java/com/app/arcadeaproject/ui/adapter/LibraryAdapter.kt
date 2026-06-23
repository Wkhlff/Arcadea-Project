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

data class LibraryGame(
    val title: String,
    val playedHours: String,
    val lastPlayed: String,
    val imageResId: Int
)

class LibraryAdapter(private val games: List<LibraryGame>) :
    RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder>() {

    class LibraryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivGame: ImageView = view.findViewById(R.id.iv_library_game_img)
        val tvTitle: TextView = view.findViewById(R.id.tv_library_game_title)
        val tvPlayedHours: TextView = view.findViewById(R.id.tv_library_played_hours)
        val tvLastPlayed: TextView = view.findViewById(R.id.tv_library_last_played)
        val btnPlay: Button = view.findViewById(R.id.btn_library_play)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.library_item_card, parent, false)
        return LibraryViewHolder(view)
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        val game = games[position]
        holder.tvTitle.text = game.title
        holder.tvPlayedHours.text = game.playedHours
        holder.tvLastPlayed.text = game.lastPlayed
        holder.ivGame.setImageResource(game.imageResId)

        holder.btnPlay.setOnClickListener {
            Toast.makeText(it.context, "Launching ${game.title}...", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = games.size
}