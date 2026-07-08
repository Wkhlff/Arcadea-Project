package com.app.arcadeaproject.ui.main.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.app.arcadeaproject.R
import com.app.arcadeaproject.data.remote.ApiClient
import com.app.arcadeaproject.ui.main.Initial.SignInActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var ivAvatar: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var tvInitial: TextView
    private lateinit var tvGamesOwned: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)

        ivAvatar = view.findViewById(R.id.iv_profile_avatar)
        tvUsername = view.findViewById(R.id.tv_profile_username)
        tvInitial = view.findViewById(R.id.tv_profile_initial)
        tvGamesOwned = view.findViewById(R.id.tv_profile_games_owned)

        loadProfileData()

        val btnBack = view.findViewById<TextView>(R.id.btn_back_profile)
        btnBack.setOnClickListener {
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
            bottomNav.selectedItemId = R.id.nav_home
        }

        val btnEditProfile = view.findViewById<Button>(R.id.btn_edit_profile)
        btnEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), ProfileEditActivity::class.java)
            startActivity(intent)
        }

        val btnSignOut = view.findViewById<Button>(R.id.btn_sign_out)
        btnSignOut.setOnClickListener {
            val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                clear()
                apply()
            }

            val intent = Intent(requireContext(), SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        loadProfileData()
    }

    private fun loadProfileData() {
        val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)
        val userName = sharedPref.getString("user_name", "User")
        val userImage = sharedPref.getString("user_image", "")

        updateUI(userName, userImage)

        if (userId != -1) {
            lifecycleScope.launch {
                // Load Profile Info
                try {
                    val profileResponse = ApiClient.instance.getProfile(userId)
                    if (profileResponse.isSuccessful && profileResponse.body()?.success == true) {
                        val user = profileResponse.body()?.user
                        if (user != null) {
                            with(sharedPref.edit()) {
                                putString("user_name", user.nama)
                                putString("user_email", user.email)
                                putString("user_image", user.gambar)
                                putString("user_bio", user.bio)
                                apply()
                            }
                            updateUI(user.nama, user.gambar)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                // Load Library for Games Owned Count
                try {
                    val libraryResponse = ApiClient.instance.getLibrary(userId)
                    if (libraryResponse.isSuccessful && libraryResponse.body()?.success == true) {
                        val gameCount = libraryResponse.body()?.games?.size ?: 0
                        tvGamesOwned.text = gameCount.toString()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateUI(name: String?, imageUrl: String?) {
        tvUsername.text = name ?: "User"
        val initial = name?.take(1)?.uppercase() ?: "U"
        tvInitial.text = initial

        if (!imageUrl.isNullOrEmpty()) {
            val fullUrl = if (imageUrl.startsWith("http")) imageUrl else "${ApiClient.BASE_URL}${imageUrl.removePrefix("/")}"

            tvInitial.visibility = View.GONE
            Glide.with(this)
                .load(fullUrl)
                .circleCrop()
                .placeholder(R.drawable.circle_avatar_bg)
                .into(ivAvatar)
        } else {
            tvInitial.visibility = View.VISIBLE
            ivAvatar.setImageResource(R.drawable.circle_avatar_bg)
        }
    }
}