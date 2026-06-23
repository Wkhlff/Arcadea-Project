package com.app.arcadeaproject.ui.main

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.arcadeaproject.R
import com.app.arcadeaproject.ui.adapter.Game
import com.app.arcadeaproject.ui.adapter.GameAdapter

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // 1. Logika Featured Card (Card Besar)
        val cardFeatured = view.findViewById<CardView>(R.id.card_featured)
        val tvOriginalPrice = view.findViewById<TextView>(R.id.tv_featured_original_price)

        // Terapkan efek coret pada harga asli
        tvOriginalPrice?.paintFlags = tvOriginalPrice?.paintFlags ?: 0 or Paint.STRIKE_THRU_TEXT_FLAG

        cardFeatured?.setOnClickListener {
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            intent.putExtra("GAME_TITLE", "Black Myth: Wukong")
            intent.putExtra("GAME_PRICE", "Rp 599.000")
            // Menggunakan logo_arcadea karena wukong tidak ditemukan
            intent.putExtra("GAME_IMAGE", R.drawable.logo_arcadea)
            startActivity(intent)
        }

        // 2. Logika RecyclerView untuk 20 item (Grid 2 Kolom)
        val rvPopular = view.findViewById<RecyclerView>(R.id.rv_popular_games)
        
        // Membuat data simulasi sebanyak 20 item menggunakan perulangan
        val gamesList = mutableListOf<Game>()
        for (i in 1..20) {
            gamesList.add(
                Game(
                    title = "Game Populer $i",
                    price = "Rp ${50 + i}.000",
                    imageResId = R.drawable.logo_arcadea
                )
            )
        }

        rvPopular?.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = GameAdapter(gamesList)
            // Matikan nested scrolling agar scroll lancar di dalam NestedScrollView
            isNestedScrollingEnabled = false 
        }

        // 3. Logika Tombol di Header
        view.findViewById<View>(R.id.btn_wishlist)?.setOnClickListener {
            Toast.makeText(requireContext(), "Opening Wishlist...", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<View>(R.id.btn_cart)?.setOnClickListener {
            Toast.makeText(requireContext(), "Opening Cart...", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
