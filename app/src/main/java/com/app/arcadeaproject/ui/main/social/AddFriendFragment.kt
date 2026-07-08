package com.app.arcadeaproject.ui.main.social

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.arcadeaproject.R
import com.app.arcadeaproject.data.remote.ApiClient
import com.app.arcadeaproject.ui.adapter.UserSearchAdapter
import kotlinx.coroutines.launch

class AddFriendFragment : Fragment() {

    private lateinit var etSearch: EditText
    private lateinit var rvResults: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    private lateinit var adapter: UserSearchAdapter
    private var friendIds: Set<Int> = emptySet()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.social_fragment_add_friend, container, false)

        etSearch = view.findViewById(R.id.et_search_user)
        rvResults = view.findViewById(R.id.rv_search_results)
        progressBar = view.findViewById(R.id.progress_bar)
        tvEmpty = view.findViewById(R.id.tv_empty_state)
        val btnBack = view.findViewById<ImageButton>(R.id.btn_back)

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        setupRecyclerView()
        loadFriendIds() // Load existing friends to mark them in search

        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(etSearch.text.toString())
                true
            } else {
                false
            }
        }

        return view
    }

    private fun setupRecyclerView() {
        adapter = UserSearchAdapter(emptyList(), friendIds) { user ->
            addFriend(user.id)
        }
        rvResults.layoutManager = LinearLayoutManager(requireContext())
        rvResults.adapter = adapter
    }

    private fun loadFriendIds() {
        val sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)
        if (userId == -1) return

        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getFriends(userId)
                if (response.isSuccessful && response.body()?.success == true) {
                    friendIds = response.body()?.friends?.map { it.id }?.toSet() ?: emptySet()
                    adapter.updateData(emptyList(), friendIds)
                }
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    private fun performSearch(query: String) {
        if (query.isEmpty()) return

        lifecycleScope.launch {
            try {
                progressBar.visibility = View.VISIBLE
                rvResults.visibility = View.GONE
                tvEmpty.visibility = View.GONE

                val response = ApiClient.instance.searchUser(query)

                if (response.isSuccessful && response.body()?.success == true) {
                    val users = response.body()?.users ?: emptyList()
                    adapter.updateData(users, friendIds)

                    if (users.isEmpty()) {
                        tvEmpty.visibility = View.VISIBLE
                    } else {
                        rvResults.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(requireContext(), "Search failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun addFriend(friendId: Int) {
        val sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)

        if (userId == -1) return

        lifecycleScope.launch {
            try {
                val request = mapOf("user_id" to userId, "friend_id" to friendId)
                val response = ApiClient.instance.addFriend(request)

                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(requireContext(), response.body()?.message ?: "Friend added!", Toast.LENGTH_SHORT).show()
                    loadFriendIds() // Refresh friends list to update UI
                } else {
                    Toast.makeText(requireContext(), response.body()?.message ?: "Failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}