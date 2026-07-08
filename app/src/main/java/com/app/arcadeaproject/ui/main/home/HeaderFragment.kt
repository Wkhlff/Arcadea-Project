package com.app.arcadeaproject.ui.main.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.arcadeaproject.R
import com.app.arcadeaproject.ui.main.CartFragment

class HeaderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment_header, container, false)

        val tvHello = view.findViewById<TextView>(R.id.tv_hello)
        
        val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("user_name", "user")
        
        tvHello.text = "Hello, $userName"

        view.findViewById<View>(R.id.btn_cart)?.setOnClickListener {
            // Gunakan supportFragmentManager dari Activity agar bisa menemukan fragmentContainer
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CartFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}