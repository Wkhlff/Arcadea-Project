package com.app.arcadeaproject.ui.main.home

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.app.arcadeaproject.R
import com.app.arcadeaproject.data.remote.ApiClient
import com.app.arcadeaproject.data.remote.model.GameResponse
import com.app.arcadeaproject.ui.main.DetailActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class Grid1Fragment : Fragment() {

    private var _view: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.home_item_grid1, container, false)
        
        // Sembunyikan root layout sampai data berhasil difilter
        _view?.visibility = View.GONE

        fetchFeaturedGame()

        return _view
    }

    private fun fetchFeaturedGame() {
        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getGames()
                if (response.isSuccessful) {
                    val games = response.body() ?: emptyList()
                    // LOGIKA: Ambil game pertama yang memiliki diskon > 0
                    val discountedGame = games.find { it.persenDiskon > 0 }
                    
                    if (discountedGame != null) {
                        _view?.visibility = View.VISIBLE
                        bindData(discountedGame)
                    } else {
                        // Jika tidak ada game diskon, sembunyikan section ini
                        _view?.visibility = View.GONE
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun bindData(game: GameResponse) {
        val view = _view ?: return
        
        val ivFeaturedImg = view.findViewById<ImageView>(R.id.iv_featured_img)
        val tvTitle = view.findViewById<TextView>(R.id.tv_featured_title)
        val tvPrice = view.findViewById<TextView>(R.id.tv_featured_price)
        val tvOriginalPrice = view.findViewById<TextView>(R.id.tv_featured_original_price)
        val tvDiscountBadge = view.findViewById<TextView>(R.id.tv_discount_badge)
        val cardFeatured = view.findViewById<CardView>(R.id.card_featured)
        val btnBuy = view.findViewById<Button>(R.id.btn_buy_now)

        tvTitle.text = game.judul
        
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        formatter.maximumFractionDigits = 0

        // Hitung harga setelah diskon
        val diskon = (game.harga * game.persenDiskon) / 100
        val hargaFinal = game.harga - diskon

        tvPrice.text = formatter.format(hargaFinal).replace("Rp", "Rp ")
        
        // Tampilkan harga asli dengan coretan
        tvOriginalPrice.visibility = View.VISIBLE
        tvOriginalPrice.text = formatter.format(game.harga).replace("Rp", "Rp ")
        tvOriginalPrice.paintFlags = tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        
        // Tampilkan badge diskon
        tvDiscountBadge.visibility = View.VISIBLE
        tvDiscountBadge.text = String.format("-%d%%", game.persenDiskon)

        Glide.with(this)
            .load(game.gambar)
            .placeholder(R.drawable.logo_arcadea)
            .into(ivFeaturedImg)

        val clickListener = View.OnClickListener {
            val intent = Intent(requireActivity(), DetailActivity::class.java).apply {
                putExtra("GAME_TITLE", game.judul)
                putExtra("GAME_PRICE", tvPrice.text.toString())
                putExtra("GAME_IMAGE_URL", game.gambar)
                putExtra("GAME_DESCRIPTION", game.deskripsi)
            }
            startActivity(intent)
        }
        
        cardFeatured.setOnClickListener(clickListener)
        btnBuy.setOnClickListener(clickListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _view = null
    }
}