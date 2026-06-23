package com.app.arcadeaproject.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.arcadeaproject.R
import com.app.arcadeaproject.ui.adapter.Game
import com.app.arcadeaproject.ui.adapter.GameAdapter

class Card2Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_card2_home, container, false)

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

        return view
    }
}