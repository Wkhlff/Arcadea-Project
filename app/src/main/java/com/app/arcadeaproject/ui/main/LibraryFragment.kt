package com.app.arcadeaproject.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.arcadeaproject.R
import com.app.arcadeaproject.data.remote.ApiClient
import com.app.arcadeaproject.ui.adapter.LibraryAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class LibraryFragment : Fragment() {

    private lateinit var adapter: LibraryAdapter
    private var currentUserId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.library_fragment, container, false)

        // Ambil ID User dari SharedPreferences
        val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        currentUserId = sharedPref.getInt("user_id", -1)

        // 1. Back Button Logic
        val btnBack = view.findViewById<TextView>(R.id.btn_back)
        btnBack?.setOnClickListener {
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
            bottomNav?.selectedItemId = R.id.nav_home
        }

        // 2. RecyclerView Setup
        val rvLibrary = view.findViewById<RecyclerView>(R.id.rv_library_games)
        adapter = LibraryAdapter(emptyList())

        rvLibrary?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = this@LibraryFragment.adapter
        }

        fetchLibraryData()

        return view
    }

    private fun fetchLibraryData() {
        if (currentUserId == -1) return

        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getLibrary(currentUserId)
                if (response.isSuccessful) {
                    val libraryResponse = response.body()
                    if (libraryResponse?.success == true) {
                        adapter.updateData(libraryResponse.games)
                    } else {
                        Toast.makeText(context, "Failed to load library", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Failed to load library", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
