package com.app.arcadeaproject.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.arcadeaproject.R
import com.app.arcadeaproject.ui.adapter.LibraryAdapter
import com.app.arcadeaproject.ui.adapter.LibraryGame
import com.google.android.material.bottomnavigation.BottomNavigationView

class LibraryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.library_fragment, container, false)

        // 1. Back Button Logic
        val btnBack = view.findViewById<TextView>(R.id.btn_back)
        btnBack?.setOnClickListener {
            // Navigate back to Home tab
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
            bottomNav?.selectedItemId = R.id.nav_home
        }

        // 2. RecyclerView Setup
        val rvLibrary = view.findViewById<RecyclerView>(R.id.rv_library_games)
        
        // Generate dynamic library data (Example: 10 games)
        val libraryGames = mutableListOf<LibraryGame>()
        for (i in 1..10) {
            libraryGames.add(
                LibraryGame(
                    title = "Library Game $i",
                    playedHours = "${i * 12} hours played",
                    lastPlayed = "Last played: ${i} days ago",
                    imageResId = R.drawable.logo_arcadea
                )
            )
        }

        rvLibrary?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = LibraryAdapter(libraryGames)
        }

        return view
    }
}