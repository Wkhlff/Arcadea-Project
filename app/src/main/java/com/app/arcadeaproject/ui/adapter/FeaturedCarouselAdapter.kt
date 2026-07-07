package com.app.arcadeaproject.ui.adapter

import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.arcadeaproject.R
import com.app.arcadeaproject.data.remote.model.GameResponse
import com.app.arcadeaproject.ui.main.DetailActivity
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.util.Locale

class FeaturedCarouselAdapter(private var games: List<GameResponse>) :
    RecyclerView.Adapter<FeaturedCarouselAdapter.CarouselViewHolder>() {

    class CarouselViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivFeaturedImg: ImageView = view.findViewById(R.id.iv_featured_img)
        val tvTitle: TextView = view.findViewById(R.id.tv_featured_title)
        val tvPrice: TextView = view.findViewById(R.id.tv_featured_price)
        val tvOriginalPrice: TextView = view.findViewById(R.id.tv_featured_original_price)
        val tvDiscountBadge: TextView = view.findViewById(R.id.tv_discount_badge)
        val btnBuy: Button = view.findViewById(R.id.btn_buy_now)
    }

    fun updateData(newGames: List<GameResponse>) {
        this.games = newGames
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_featured_carousel, parent, false)
        return CarouselViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val game = games[position]
        
        holder.tvTitle.text = game.judul
        
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        formatter.maximumFractionDigits = 0

        val diskon = (game.harga * game.persenDiskon) / 100
        val hargaFinal = game.harga - diskon

        holder.tvPrice.text = formatter.format(hargaFinal).replace("Rp", "Rp ")
        
        holder.tvOriginalPrice.visibility = View.VISIBLE
        holder.tvOriginalPrice.text = formatter.format(game.harga).replace("Rp", "Rp ")
        holder.tvOriginalPrice.paintFlags = holder.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        
        holder.tvDiscountBadge.visibility = View.VISIBLE
        holder.tvDiscountBadge.text = String.format("-%d%%", game.persenDiskon)

        Glide.with(holder.itemView.context)
            .load(game.gambar)
            .placeholder(R.drawable.logo_arcadea)
            .into(holder.ivFeaturedImg)

        val clickListener = View.OnClickListener {
            val intent = Intent(it.context, DetailActivity::class.java).apply {
                putExtra("GAME_TITLE", game.judul)
                putExtra("GAME_PRICE", holder.tvPrice.text.toString())
                putExtra("GAME_IMAGE_URL", game.gambar)
                putExtra("GAME_DESCRIPTION", game.deskripsi)
            }
            it.context.startActivity(intent)
        }
        
        holder.itemView.setOnClickListener(clickListener)
        holder.btnBuy.setOnClickListener(clickListener)
    }

    override fun getItemCount(): Int = games.size
}
