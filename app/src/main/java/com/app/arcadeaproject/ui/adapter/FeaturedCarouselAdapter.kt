package com.app.arcadeaproject.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.arcadeaproject.R
import com.app.arcadeaproject.data.local.DatabaseHelper
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
        val context = holder.itemView.context
        val dbHelper = DatabaseHelper(context)
        
        holder.tvTitle.text = game.judul
        
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        formatter.maximumFractionDigits = 0

        val diskon = (game.harga * game.persenDiskon) / 100
        val hargaFinal = game.harga - diskon
        val formattedPrice = formatter.format(hargaFinal).replace("Rp", "Rp ")

        holder.tvPrice.text = formattedPrice
        
        holder.tvOriginalPrice.visibility = View.VISIBLE
        holder.tvOriginalPrice.text = formatter.format(game.harga).replace("Rp", "Rp ")
        holder.tvOriginalPrice.paintFlags = holder.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        
        holder.tvDiscountBadge.visibility = View.VISIBLE
        holder.tvDiscountBadge.text = String.format("-%d%%", game.persenDiskon)

        Glide.with(context)
            .load(game.gambar)
            .placeholder(R.drawable.logo_arcadea)
            .into(holder.ivFeaturedImg)

        // Klik Item untuk Detail
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("GAME_ID", game.id)
                putExtra("GAME_TITLE", game.judul)
                putExtra("GAME_PRICE", formattedPrice)
                putExtra("GAME_IMAGE_URL", game.gambar)
                putExtra("GAME_DESCRIPTION", game.deskripsi)
            }
            context.startActivity(intent)
        }

        // Klik Button untuk Add To Cart
        holder.btnBuy.setOnClickListener {
            val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val userId = sharedPref.getInt("user_id", -1)

            if (userId != -1) {
                dbHelper.addToCart(
                    userId, 
                    game.id, 
                    game.judul, 
                    formattedPrice, 
                    game.gambar, 
                    game.deskripsi ?: ""
                )
                Toast.makeText(context, "${game.judul} added to Cart", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Please login first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = games.size
}
