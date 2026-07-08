package com.app.arcadeaproject.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.arcadeaproject.R
import com.app.arcadeaproject.data.remote.ApiClient
import com.app.arcadeaproject.ui.adapter.GameAdapter
import kotlinx.coroutines.launch

class Grid2Fragment : Fragment() {

    private lateinit var gameAdapter: GameAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_item_grid2, container, false)
        val rvPopular = view.findViewById<RecyclerView>(R.id.rv_popular_games)
        
        gameAdapter = GameAdapter(emptyList())

        rvPopular?.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = gameAdapter
            isNestedScrollingEnabled = false
        }

        fetchGames()
        return view
    }

    private fun fetchGames() {
        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getGames()
                if (response.isSuccessful) {
                    val allGames = response.body() ?: emptyList()
                    // Menampilkan game yang tidak diskon (Populer)
                    val nonDiscountedGames = allGames.filter { it.persenDiskon == 0 }
                    gameAdapter.updateData(nonDiscountedGames)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
