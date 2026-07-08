package com.app.arcadeaproject.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.arcadeaproject.R
import com.app.arcadeaproject.data.remote.ApiClient
import com.app.arcadeaproject.data.remote.model.RefundRequest
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
        adapter = LibraryAdapter(emptyList()) { game ->
            showRefundConfirmation(game.id)
        }

        rvLibrary?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = this@LibraryFragment.adapter
        }

        fetchLibraryData()

        return view
    }

    private fun showRefundConfirmation(gameId: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Refund Game")
            .setMessage("Are you sure you want to refund this game?")
            .setPositiveButton("Yes") { _, _ ->
                processRefund(gameId)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun processRefund(gameId: Int) {
        if (currentUserId == -1) return

        lifecycleScope.launch {
            try {
                val request = RefundRequest(currentUserId, gameId)
                val response = ApiClient.instance.refundGame(request)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(context, "Refund successful", Toast.LENGTH_SHORT).show()
                    fetchLibraryData() // Refresh list
                } else {
                    val errorMsg = response.body()?.message ?: "Refund failed"
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
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
