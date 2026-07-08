package com.app.arcadeaproject.ui.main.social

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.arcadeaproject.R
import com.app.arcadeaproject.data.remote.ApiClient
import com.app.arcadeaproject.data.remote.model.Post
import com.app.arcadeaproject.ui.adapter.CommunityAdapter
import com.app.arcadeaproject.ui.adapter.FriendsAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class SocialFragment : Fragment() {

    private lateinit var rvSocial: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmptyState: TextView
    private lateinit var etSearch: EditText
    
    private lateinit var friendsAdapter: FriendsAdapter
    private lateinit var communityAdapter: CommunityAdapter

    private lateinit var btnTabFriends: Button
    private lateinit var btnTabCommunity: Button
    
    private var currentTab = "friends"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_social, container, false)

        rvSocial = view.findViewById(R.id.rv_friends_list)
        progressBar = view.findViewById(R.id.progress_bar_social)
        tvEmptyState = view.findViewById(R.id.tv_no_friends)
        etSearch = view.findViewById(R.id.et_search_friends)

        btnTabFriends = view.findViewById(R.id.btn_tab_friends)
        btnTabCommunity = view.findViewById(R.id.btn_tab_community)

        val btnBack = view.findViewById<TextView>(R.id.btn_back_social)
        btnBack.setOnClickListener {
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
            bottomNav.selectedItemId = R.id.nav_home
        }

        val btnAddFriend = view.findViewById<Button>(R.id.btn_add_friend)
        btnAddFriend.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddFriendFragment())
                .addToBackStack(null)
                .commit()
        }

        setupRecyclerView()
        setupTabs()

        // Initial load
        loadFriendsList()

        return view
    }

    private fun setupTabs() {
        btnTabFriends.setOnClickListener {
            if (currentTab != "friends") {
                currentTab = "friends"
                updateTabUI(btnTabFriends)
                etSearch.visibility = View.VISIBLE
                etSearch.hint = "Search friends..."
                rvSocial.adapter = friendsAdapter
                loadFriendsList()
            }
        }
        btnTabCommunity.setOnClickListener {
            if (currentTab != "community") {
                currentTab = "community"
                updateTabUI(btnTabCommunity)
                etSearch.visibility = View.GONE
                rvSocial.adapter = communityAdapter
                loadCommunityPosts()
            }
        }
    }

    private fun updateTabUI(selectedBtn: Button) {
        val buttons = listOf(btnTabFriends, btnTabCommunity)
        buttons.forEach { btn ->
            if (btn == selectedBtn) {
                btn.setBackgroundColor(Color.parseColor("#A44AFF"))
            } else {
                btn.setBackgroundColor(Color.parseColor("#2A475E"))
            }
        }
    }

    private fun setupRecyclerView() {
        friendsAdapter = FriendsAdapter(emptyList()) { friend ->
            val chatFragment = ChatFragment().apply {
                arguments = Bundle().apply {
                    putInt("receiver_id", friend.id)
                    putString("receiver_name", friend.nama)
                    putString("receiver_avatar", friend.fotoProfile)
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, chatFragment)
                .addToBackStack(null)
                .commit()
        }
        
        communityAdapter = CommunityAdapter(emptyList())
        
        rvSocial.layoutManager = LinearLayoutManager(requireContext())
        rvSocial.adapter = friendsAdapter
    }

    private fun loadFriendsList() {
        val sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)

        if (userId == -1) {
            Toast.makeText(requireContext(), "User session not found, please login again", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                progressBar.visibility = View.VISIBLE
                rvSocial.visibility = View.GONE
                tvEmptyState.visibility = View.GONE

                val response = ApiClient.instance.getFriends(userId)

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.success) {
                        val friends = body.friends ?: emptyList()
                        friendsAdapter.updateData(friends)

                        if (friends.isEmpty()) {
                            tvEmptyState.text = "No friends yet"
                            tvEmptyState.visibility = View.VISIBLE
                            rvSocial.visibility = View.GONE
                        } else {
                            tvEmptyState.visibility = View.GONE
                            rvSocial.visibility = View.VISIBLE
                        }
                    } else {
                        Toast.makeText(requireContext(), "Backend error: ${body.success}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Failed to load friends (Code: ${response.code()})"
                    Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Connection error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun loadCommunityPosts() {
        progressBar.visibility = View.VISIBLE
        tvEmptyState.visibility = View.GONE
        
        // Dummy data based on provided screenshots
        val dummyPosts = listOf(
            Post(1, "Yomandiguna", "Cyberpunk 2077", "Night City is breathtaking!", R.drawable.ic_launcher_foreground, 420, 24),
            Post(2, "Khalif", "GTA V", "Finally got the new car!", R.drawable.ic_launcher_foreground, 156, 10),
            Post(3, "Mbut", "Dota 2", "Road to Immortal starts now.", R.drawable.ic_launcher_foreground, 89, 45)
        )
        
        communityAdapter.updateData(dummyPosts)
        
        progressBar.visibility = View.GONE
        rvSocial.visibility = View.VISIBLE
    }
}
