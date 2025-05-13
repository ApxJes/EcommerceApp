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
import com.example.ecommerceapp.presentation.viewMdoel.CartViewModel
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
        cartAdapter = CartAdapter {
            val subTotal = cartAdapter.getTotalPrice()
            val deliveryFee = 10.0
            val total = subTotal + deliveryFee

            binding.txvPrice.text = "$${subTotal}"
            binding.txvDeliveryFee.text = "$${deliveryFee}"
            binding.txvTotalPrice.text = "$"+ String.format("%.2f", total)
        }

        binding.rcvCart.layoutManager = LinearLayoutManager(requireContext())
        binding.rcvCart.adapter = cartAdapter

        cartViewModel.cartItem.onEach {
            cartAdapter.setItem(it)
            val subTotal = cartAdapter.getTotalPrice()
            val deliveryFees = 10.0
            val totalPrice = subTotal + deliveryFees

            binding.txvPrice.text = "$${subTotal}"
            binding.txvDeliveryFee.text = "$${deliveryFees}"
            binding.txvTotalPrice.text = "$"+ String.format("%.2f", totalPrice)

        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.imvBack.setOnClickListener {
            findNavController().navigate(
                R.id.homeFragment, null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.onBoardingFragment, true)
                    .build()
            )
        }

        binding.btnCheckOut.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Thanks for ordering",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}