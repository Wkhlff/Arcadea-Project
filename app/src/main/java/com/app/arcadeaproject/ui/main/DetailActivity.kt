package com.app.arcadeaproject.ui.main

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.app.arcadeaproject.R
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val title = intent.getStringExtra("GAME_TITLE")
        val price = intent.getStringExtra("GAME_PRICE")
        val imageUrl = intent.getStringExtra("GAME_IMAGE_URL")
        val description = intent.getStringExtra("GAME_DESCRIPTION")

        val tvTitle = findViewById<TextView>(R.id.tv_detail_title)
        val tvPrice = findViewById<TextView>(R.id.tv_detail_price)
        val tvDescription = findViewById<TextView>(R.id.tv_detail_description)
        val ivCover = findViewById<ImageView>(R.id.iv_detail_cover)
        val btnBack = findViewById<ImageButton>(R.id.btn_back_detail)

        tvTitle.text = title
        tvPrice.text = price
        tvDescription.text = description

        // Load gambar dari URL menggunakan Glide di halaman detail
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.logo_arcadea)
            .into(ivCover)

        btnBack.setOnClickListener {
            finish()
        }
    }
}
