package com.app.arcadeaproject

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val title = intent.getStringExtra("GAME_TITLE")
        val price = intent.getStringExtra("GAME_PRICE")

        val tvTitle = findViewById<TextView>(R.id.tv_detail_title)
        val tvPrice = findViewById<TextView>(R.id.tv_detail_price)
        val btnBack = findViewById<ImageButton>(R.id.btn_back_detail)

        tvTitle.text = title
        tvPrice.text = price

        btnBack.setOnClickListener {
            finish()
        }
    }
}