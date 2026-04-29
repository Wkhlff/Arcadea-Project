package com.app.arcadeaproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install Splash Screen before super.onCreate and setContentView
        installSplashScreen()
        
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val btnSignIn = findViewById<Button>(R.id.btn_sign_in)
        val tvGoToRegister = findViewById<TextView>(R.id.tv_go_to_register)

        btnSignIn.setOnClickListener {
            Toast.makeText(this, "Welcome to Arcadea!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        tvGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}