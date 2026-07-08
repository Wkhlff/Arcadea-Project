package com.app.arcadeaproject.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.app.arcadeaproject.R
import com.app.arcadeaproject.data.remote.ApiClient
import com.app.arcadeaproject.ui.adapter.FeaturedCarouselAdapter
import com.app.arcadeaproject.ui.main.WishlistFragment
import kotlinx.coroutines.launch
import kotlin.math.abs

class Grid1Fragment : Fragment() {

    private var _view: View? = null
    private lateinit var carouselAdapter: FeaturedCarouselAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.home_item_grid1, container, false)
        
        val viewPager = _view?.findViewById<ViewPager2>(R.id.vp_featured_carousel)
        val btnWishlist = _view?.findViewById<TextView>(R.id.btn_wishlist)

        carouselAdapter = FeaturedCarouselAdapter(emptyList())
        
        viewPager?.apply {
            adapter = carouselAdapter
            offscreenPageLimit = 3
            clipToPadding = false
            clipChildren = false
            
            // Set padding agar item di kanan/kiri terlihat (efek kepotong)
            val paddingPx = resources.getDimensionPixelOffset(R.dimen.carousel_padding)
            setPadding(paddingPx, 0, paddingPx, 0)

            val transformer = CompositePageTransformer()
            transformer.addTransformer(MarginPageTransformer(resources.getDimensionPixelOffset(R.dimen.carousel_margin)))
            transformer.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.85f + r * 0.15f
            }
            setPageTransformer(transformer)
        }

        btnWishlist?.setOnClickListener {
            // Menggunakan activity supportFragmentManager agar bisa menemukan fragmentContainer di MainActivity
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, WishlistFragment())
                .addToBackStack(null)
                .commit()
        }

        fetchFeaturedGames()

        return _view
    }

    private fun fetchFeaturedGames() {
        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getGames()
                if (response.isSuccessful) {
                    val games = response.body() ?: emptyList()
                    val discountedGames = games.filter { it.persenDiskon > 0 }
                    
                    if (discountedGames.isNotEmpty()) {
                        _view?.visibility = View.VISIBLE
                        carouselAdapter.updateData(discountedGames)
                    } else {
                        _view?.visibility = View.GONE
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _view = null
    }
}
