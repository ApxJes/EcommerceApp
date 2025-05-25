package com.example.ecommerceapp.presentation.ui.fragment.mainScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentCartBinding
import com.example.ecommerceapp.presentation.adapter.CartAdapter
import com.example.ecommerceapp.presentation.viewMdoel.local.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartAdapter = CartAdapter(
            onQuantityChanged = {
                val subTotal = cartAdapter.getTotalPrice()
                val deliveryFee = 10.0
                val total = subTotal + deliveryFee

                binding.txvPrice.text = "$"+ String.format("%.2f", subTotal)
                binding.txvDeliveryFee.text = "$${deliveryFee}"
                binding.txvTotalPrice.text = "$" + String.format("%.2f", total)
            },

            onItemsRemove = { product ->
                cartViewModel.removeCart(product)
            }
        )

        binding.rcvCart.layoutManager = LinearLayoutManager(requireContext())
        binding.rcvCart.adapter = cartAdapter

        cartViewModel.cartItem.onEach {
            cartAdapter.setItem(it)
            val subTotal = cartAdapter.getTotalPrice()
            val deliveryFees = 10.0
            val totalPrice = subTotal + deliveryFees

            binding.txvPrice.text = "$"+ String.format("%.2f", subTotal)
            binding.txvDeliveryFee.text = "$${deliveryFees}"
            binding.txvTotalPrice.text = "$"+ String.format("%.2f", totalPrice)

            if(it.isEmpty()) {
                binding.emptyLayout.visibility = View.VISIBLE
                binding.rcvCart.visibility = View.GONE
            } else {
                binding.emptyLayout.visibility = View.GONE
                binding.rcvCart.visibility = View.VISIBLE
            }

        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.btnCheckOut.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Thanks for ordering",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnCheckOut1.setOnClickListener {
            if(cartAdapter.itemCount > 0) {
                binding.constraintLayout.visibility = View.VISIBLE
                binding.btnCheckOut1.visibility = View.GONE
            }
        }

        if(cartAdapter.itemCount <= 0) {
            binding.btnCheckOut1.visibility = View.GONE
        }

        cartAdapter.setOnClickListener {
            findNavController().navigate(
                CartFragmentDirections.actionCartFragmentToProductDetailsFragment(it)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}