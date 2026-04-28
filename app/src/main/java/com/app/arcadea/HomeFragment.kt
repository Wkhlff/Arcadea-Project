package com.app.arcadea

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val cardGta = view.findViewById<CardView>(R.id.card_gta)
        val cardHades = view.findViewById<CardView>(R.id.card_hades)

        cardGta.setOnClickListener {
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            intent.putExtra("GAME_TITLE", "Grand Theft Auto VI")
            intent.putExtra("GAME_PRICE", "Coming Soon")
            startActivity(intent)
        }

        cardHades.setOnClickListener {
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            intent.putExtra("GAME_TITLE", "Hades")
            intent.putExtra("GAME_PRICE", "Rp 119.999")
            startActivity(intent)
        }

        val btnWishlist = view.findViewById<View>(R.id.btn_wishlist)
        val btnCart = view.findViewById<View>(R.id.btn_cart)

        btnWishlist.setOnClickListener {
            Toast.makeText(requireContext(), "Opening Wishlist...", Toast.LENGTH_SHORT).show()
        }
        btnCart.setOnClickListener {
            Toast.makeText(requireContext(), "Opening Cart...", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}