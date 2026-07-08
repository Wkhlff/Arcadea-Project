package com.app.arcadeaproject.ui.main

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.arcadeaproject.R
import com.app.arcadeaproject.data.local.DatabaseHelper
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        dbHelper = DatabaseHelper(this)

        val id = intent.getIntExtra("GAME_ID", -1)
        val title = intent.getStringExtra("GAME_TITLE") ?: ""
        val price = intent.getStringExtra("GAME_PRICE") ?: ""
        val imageUrl = intent.getStringExtra("GAME_IMAGE_URL") ?: ""
        val description = intent.getStringExtra("GAME_DESCRIPTION") ?: ""

        val tvTitle = findViewById<TextView>(R.id.tv_detail_title)
        val tvPrice = findViewById<TextView>(R.id.tv_detail_price)
        val tvDescription = findViewById<TextView>(R.id.tv_detail_description)
        val ivCover = findViewById<ImageView>(R.id.iv_detail_cover)
        val btnBack = findViewById<ImageButton>(R.id.btn_back_detail)
        val btnAddToCart = findViewById<Button>(R.id.btn_add_to_cart_detail)
        val btnAddToWishlist = findViewById<Button>(R.id.btn_add_to_wishlist_detail)

        tvTitle.text = title
        tvPrice.text = price
        tvDescription.text = description

        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.logo_arcadea)
            .into(ivCover)

        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)

        btnAddToCart.setOnClickListener {
            if (userId != -1 && id != -1) {
                dbHelper.addToCart(userId, id, title, price, imageUrl, description)
                Toast.makeText(this, "$title added to Cart", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
            }
        }

        btnAddToWishlist.setOnClickListener {
            if (userId != -1 && id != -1) {
                dbHelper.addToWishlist(userId, id, title, price, imageUrl, description)
                Toast.makeText(this, "$title added to Wishlist", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}