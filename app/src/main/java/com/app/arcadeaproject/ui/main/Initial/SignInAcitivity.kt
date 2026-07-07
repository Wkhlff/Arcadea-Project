package com.app.arcadeaproject.ui.main.Initial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.app.arcadeaproject.MainActivity
import com.app.arcadeaproject.R
import com.app.arcadeaproject.data.remote.ApiClient
import com.app.arcadeaproject.data.remote.model.LoginRequest
import kotlinx.coroutines.launch

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val btnSignIn = findViewById<Button>(R.id.btn_sign_in)
        val tvGoToRegister = findViewById<TextView>(R.id.tv_go_to_register)

        btnSignIn.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    btnSignIn.isEnabled = false
                    val response = ApiClient.instance.login(LoginRequest(email, password))

                    if (response.isSuccessful) {
                        val authResponse = response.body()
                        if (authResponse?.success == true) {
                            // Simpan nama user ke SharedPreferences
                            val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                            with(sharedPref.edit()) {
                                putString("user_name", authResponse.user?.nama ?: "User")
                                apply()
                            }

                            Toast.makeText(this@SignInActivity, "Login Successful!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@SignInActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@SignInActivity, authResponse?.message ?: "Login Failed", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@SignInActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@SignInActivity, "Connection error: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    btnSignIn.isEnabled = true
                }
            }
        }

        tvGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
