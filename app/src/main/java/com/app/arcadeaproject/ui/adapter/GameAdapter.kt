package com.app.arcadeaproject.ui.adapter

import android.content.Intent
import android.graphics.Paint
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
import java.text.NumberFormat
import java.util.Locale

class GameAdapter(
    private var games: List<GameResponse>,
    private val layoutResId: Int = R.layout.home_item_grid
) : RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivGame: ImageView = view.findViewById(R.id.iv_game_img)
        val tvTitle: TextView = view.findViewById(R.id.tv_game_title)
        val tvPrice: TextView = view.findViewById(R.id.tv_game_price)
        val tvOriginalPrice: TextView? = view.findViewById(R.id.tv_game_original_price)
        val tvDiscountBadge: TextView? = view.findViewById(R.id.tv_discount_badge)
    }

    fun updateData(newGames: List<GameResponse>) {
        this.games = newGames
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.tvTitle.text = game.judul
        
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        formatter.maximumFractionDigits = 0

        if (game.persenDiskon > 0) {
            val diskon = (game.harga * game.persenDiskon) / 100
            val hargaFinal = game.harga - diskon
            holder.tvPrice.text = formatter.format(hargaFinal).replace("Rp", "Rp ")
            
            holder.tvOriginalPrice?.apply {
                visibility = View.VISIBLE
                text = formatter.format(game.harga).replace("Rp", "Rp ")
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            
            holder.tvDiscountBadge?.apply {
                visibility = View.VISIBLE
                text = "-${game.persenDiskon}%"
            }
        } else {
            holder.tvPrice.text = formatter.format(game.harga).replace("Rp", "Rp ")
            holder.tvOriginalPrice?.visibility = View.GONE
            holder.tvDiscountBadge?.visibility = View.GONE
        }

        Glide.with(holder.itemView.context)
            .load(game.gambar)
            .placeholder(R.drawable.logo_arcadea)
            .into(holder.ivGame)

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra("GAME_TITLE", game.judul)
            intent.putExtra("GAME_PRICE", holder.tvPrice.text.toString())
            intent.putExtra("GAME_IMAGE_URL", game.gambar)
            intent.putExtra("GAME_DESCRIPTION", game.deskripsi)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount() = games.size
}