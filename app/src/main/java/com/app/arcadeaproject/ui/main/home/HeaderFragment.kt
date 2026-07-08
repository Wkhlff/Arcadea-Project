package com.app.arcadeaproject.ui.main.home

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.arcadeaproject.R
import com.app.arcadeaproject.ui.main.cart.CartFragment
import com.app.arcadeaproject.ui.main.SearchResultsFragment

class HeaderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment_header, container, false)

        val tvHello = view.findViewById<TextView>(R.id.tv_hello)
        val etSearch = view.findViewById<EditText>(R.id.et_search)
        
        val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("user_name", "user")
        
        tvHello.text = "Hello, $userName"

        view.findViewById<View>(R.id.btn_cart)?.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CartFragment())
                .addToBackStack(null)
                .commit()
        }

        // Implementasi Fitur Search
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isAdded) return
                
                val query = s.toString().trim()
                
                // Cari container di level activity agar lebih stabil
                val homeContent = requireActivity().findViewById<View>(R.id.layout_home_content)
                val searchContainer = requireActivity().findViewById<View>(R.id.container_search_results)

                if (query.isEmpty()) {
                    homeContent?.visibility = View.VISIBLE
                    searchContainer?.visibility = View.GONE
                } else {
                    homeContent?.visibility = View.GONE
                    searchContainer?.visibility = View.VISIBLE

                    // Gunakan childFragmentManager dari parent fragment (HomeFragment)
                    val manager = parentFragmentManager
                    var searchFragment = manager.findFragmentById(R.id.container_search_results) as? SearchResultsFragment
                    
                    if (searchFragment == null) {
                        searchFragment = SearchResultsFragment()
                        manager.beginTransaction()
                            .replace(R.id.container_search_results, searchFragment)
                            .commit()
                    }
                    
                    searchFragment.updateSearchQuery(query)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }
}
