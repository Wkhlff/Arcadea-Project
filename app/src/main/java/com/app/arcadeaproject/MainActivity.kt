package com.app.arcadeaproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.arcadeaproject.ui.main.BottomFragment

class MainActivity : AppCompatActivity() {

    private val loginReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "ACTION_LOGIN_SUCCESS") {
                val userName = intent.getStringExtra("USER_NAME") ?: "User"
                Toast.makeText(context, "Welcome back, $userName! (Broadcast Received)", Toast.LENGTH_LONG).show()
            }
        }
    }

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

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter("ACTION_LOGIN_SUCCESS")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(loginReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            @Suppress("UnspecifiedRegisterReceiverFlag")
            registerReceiver(loginReceiver, filter)
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(loginReceiver)
    }
}