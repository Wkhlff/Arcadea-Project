package com.app.arcadeaproject

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.arcadeaproject.ui.main.BottomFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        val container = findViewById<android.view.View>(R.id.fragmentContainer)
        if (container != null) {
            ViewCompat.setOnApplyWindowInsetsListener(container) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        if (savedInstanceState == null) {
            // Gunakan BottomFragment sebagai entry point utama di MainActivity
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, BottomFragment())
                .commit()
        }
    }
}