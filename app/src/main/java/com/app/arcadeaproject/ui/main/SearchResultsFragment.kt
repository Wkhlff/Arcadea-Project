package com.app.arcadeaproject.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.app.arcadeaproject.data.remote.ApiClient
import com.app.arcadeaproject.data.remote.model.GameResponse
import com.app.arcadeaproject.databinding.SocialFragmentSearchResultsBinding
import com.app.arcadeaproject.ui.adapter.GameAdapter
import kotlinx.coroutines.launch

class SearchResultsFragment : Fragment() {

    private var _binding: SocialFragmentSearchResultsBinding? = null
    private val binding get() = _binding!!

    private var searchAdapter: GameAdapter? = null
    private var allGames: List<GameResponse> = emptyList()
    private var pendingQuery: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SocialFragmentSearchResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchAdapter = GameAdapter(emptyList())
        binding.rvSearchResults.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = searchAdapter
        }

        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getGames()
                if (response.isSuccessful) {
                    allGames = response.body() ?: emptyList()
                    pendingQuery?.let {
                        val q = it
                        pendingQuery = null
                        updateSearchQuery(q)
                    }
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun updateSearchQuery(query: String) {
        if (_binding == null || searchAdapter == null) {
            pendingQuery = query
            return
        }

        if (query.isEmpty()) {
            searchAdapter?.updateData(emptyList())
            binding.tvNoResults.visibility = View.GONE
            return
        }

        val filteredList = allGames.filter { it.judul.contains(query, ignoreCase = true) }
        searchAdapter?.updateData(filteredList)

        val isEmpty = filteredList.isEmpty() && allGames.isNotEmpty()
        binding.tvNoResults.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.rvSearchResults.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        searchAdapter = null
    }
}
