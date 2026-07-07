package com.app.arcadeaproject.ui.main.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.arcadeaproject.R

class HeaderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment_header, container, false)

        val tvHello = view.findViewById<TextView>(R.id.tv_hello)
        
        // Ambil nama dari SharedPreferences
        val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("user_name", "user")
        
        // Update text Hello, [Nama]
        tvHello.text = "Hello, $userName"

        // Logika untuk tombol Cart yang ada di header
        view.findViewById<View>(R.id.btn_cart)?.setOnClickListener {
            Toast.makeText(requireContext(), "Opening Cart...", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}