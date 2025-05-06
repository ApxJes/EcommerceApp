package com.example.ecommerceapp.presentation.ui.fragment.categories

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerceapp.databinding.FragmentFurnitureBinding
import com.example.ecommerceapp.presentation.adapter.GetProductsAdapter
import com.example.ecommerceapp.presentation.viewMdoel.GetProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class FurnitureFragment : Fragment() {

    private var _binding: FragmentFurnitureBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GetProductsViewModel by viewModels()
    private lateinit var furnitureAdapter: GetProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFurnitureBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        furnitureAdapter = GetProductsAdapter()
        setUpRecyclerViewForFashion()
        observeFurnitureProducts()
        viewModel.getProductsByCategory(category = "furniture")

        binding.imvBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        furnitureAdapter.setOnClickListener {
            val action = FragrancesFragmentDirections
                .actionFragrancesFragmentToProductDetailsFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun observeFurnitureProducts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { productState ->
                    binding.loadingPrg.visibility =
                        if (productState.isLoading) View.VISIBLE else View.GONE

                    furnitureAdapter.differ.submitList(productState.products)
                    Log.d("FashionFragment", "Categories: ${productState.products.map { it.category }}")

                    binding.txvEmptyState.visibility =
                        if(productState.products.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.event.collect { event ->
                when (event) {
                    is GetProductsViewModel.UiEvent.ToastMessage -> {
                        Toast.makeText(
                            requireContext(),
                            event.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun setUpRecyclerViewForFashion() {
        binding.rcvFurniture.apply {
            adapter = furnitureAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}