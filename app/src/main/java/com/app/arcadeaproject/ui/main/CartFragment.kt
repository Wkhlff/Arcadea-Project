package com.app.arcadeaproject.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.arcadeaproject.data.local.DatabaseHelper
import com.app.arcadeaproject.data.remote.ApiClient
import com.app.arcadeaproject.data.remote.model.CheckoutRequest
import com.app.arcadeaproject.databinding.CartFragmentBinding
import com.app.arcadeaproject.ui.adapter.CartAdapter
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class CartFragment : Fragment() {

    private var _binding: CartFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: CartAdapter
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CartFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DatabaseHelper(requireContext())
        
        val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        userId = sharedPref.getInt("user_id", -1)

        setupRecyclerView()
        updateCartInfo()

        binding.btnBackCart.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnPurchase.setOnClickListener {
            performCheckout()
        }
    }

    private fun performCheckout() {
        if (userId == -1) {
            Toast.makeText(requireContext(), "Please login first", Toast.LENGTH_SHORT).show()
            return
        }

        val cartItems = dbHelper.getAllCart(userId)
        if (cartItems.isEmpty()) {
            Toast.makeText(requireContext(), "Your cart is empty", Toast.LENGTH_SHORT).show()
            return
        }

        val gameIds = cartItems.map { it.id }
        val request = CheckoutRequest(userId, gameIds)

        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.checkout(request)
                if (response.isSuccessful && response.body()?.success == true) {
                    dbHelper.clearCart(userId)
                    updateCartInfo()
                    Toast.makeText(requireContext(), "Purchase Successful! Games added to library.", Toast.LENGTH_LONG).show()
                } else {
                    val errorMsg = response.body()?.message ?: "Checkout failed"
                    Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        val cartItems = if (userId != -1) dbHelper.getAllCart(userId) else emptyList()
        adapter = CartAdapter(cartItems) { game ->
            dbHelper.removeFromCart(userId, game.id)
            updateCartInfo()
        }
        binding.rvCartItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCartItems.adapter = adapter
    }

    private fun updateCartInfo() {
        if (userId == -1) {
            binding.tvEmptyCart.visibility = View.VISIBLE
            binding.layoutCheckout.visibility = View.GONE
            return
        }

        val items = dbHelper.getAllCart(userId)
        if (items.isEmpty()) {
            binding.tvEmptyCart.visibility = View.VISIBLE
            binding.layoutCheckout.visibility = View.GONE
        } else {
            binding.tvEmptyCart.visibility = View.GONE
            binding.layoutCheckout.visibility = View.VISIBLE
            
            var totalPrice = 0L
            for (item in items) {
                val cleanPrice = item.price.replace(Regex("[^0-9]"), "")
                totalPrice += cleanPrice.toLongOrNull() ?: 0L
            }
            
            val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            formatter.maximumFractionDigits = 0
            binding.tvTotalPrice.text = formatter.format(totalPrice).replace("Rp", "Rp ")
        }
        adapter.updateData(items)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
