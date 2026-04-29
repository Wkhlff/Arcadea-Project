package com.app.arcadeaproject

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val title = intent.getStringExtra("GAME_TITLE")
        val price = intent.getStringExtra("GAME_PRICE")
        val imageResId = intent.getIntExtra("GAME_IMAGE", R.drawable.wukong)

        val tvTitle = findViewById<TextView>(R.id.tv_detail_title)
        val tvPrice = findViewById<TextView>(R.id.tv_detail_price)
        val ivCover = findViewById<ImageView>(R.id.iv_detail_cover)
        val btnBack = findViewById<ImageButton>(R.id.btn_back_detail)

        tvTitle.text = title
        tvPrice.text = price
        ivCover.setImageResource(imageResId)

        btnBack.setOnClickListener {
            finish()
        }
    }
}