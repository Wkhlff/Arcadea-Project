package com.app.arcadeaproject.ui.main.home

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.app.arcadeaproject.R
import com.app.arcadeaproject.ui.main.DetailActivity

class Grid1Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_item_grid1, container, false)

        val cardFeatured = view.findViewById<CardView>(R.id.card_featured)
        val tvOriginalPrice = view.findViewById<TextView>(R.id.tv_featured_original_price)
        val btnWishlist = view.findViewById<View>(R.id.btn_wishlist)

        // Efek coret harga asli
        tvOriginalPrice?.paintFlags = (tvOriginalPrice?.paintFlags ?: 0) or Paint.STRIKE_THRU_TEXT_FLAG

        cardFeatured?.setOnClickListener {
            val intent = Intent(requireActivity(), DetailActivity::class.java).apply {
                putExtra("GAME_TITLE", "Black Myth: Wukong")
                putExtra("GAME_PRICE", "Rp 599.000")
                putExtra("GAME_IMAGE", R.drawable.logo_arcadea)
            }
            startActivity(intent)
        }

        btnWishlist?.setOnClickListener {
            Toast.makeText(requireContext(), "Opening Wishlist...", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}