package com.app.arcadeaproject.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.arcadeaproject.data.local.DatabaseHelper
import com.app.arcadeaproject.databinding.WishlistFragmentBinding
import com.app.arcadeaproject.ui.adapter.WishlistAdapter

class WishlistFragment : Fragment() {

    private var _binding: WishlistFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: WishlistAdapter
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WishlistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DatabaseHelper(requireContext())

        val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        userId = sharedPref.getInt("user_id", -1)

        setupRecyclerView()

        binding.toolbarWishlist.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupRecyclerView() {
        val wishlistItems = if (userId != -1) dbHelper.getAllWishlist(userId) else emptyList()
        adapter = WishlistAdapter(
            wishlistItems,
            onRemoveClick = { game ->
                dbHelper.removeFromWishlist(userId, game.id)
                updateWishlist()
                Toast.makeText(context, "${game.title} removed from Wishlist", Toast.LENGTH_SHORT).show()
            },
            onAddToCartClick = { game ->
                dbHelper.addToCart(userId, game.id, game.title, game.price, game.image, game.description)
                dbHelper.removeFromWishlist(userId, game.id)
                updateWishlist()
                Toast.makeText(context, "${game.title} moved to Cart", Toast.LENGTH_SHORT).show()
            }
        )
        binding.rvWishlist.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWishlist.adapter = adapter
    }

    private fun updateWishlist() {
        if (userId != -1) {
            adapter.updateData(dbHelper.getAllWishlist(userId))
        }
    }

    override fun onResume() {
        super.onResume()
        updateWishlist()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
