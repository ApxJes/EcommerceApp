package com.example.ecommerceapp.presentation.ui.fragment.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerceapp.databinding.FragmentFashionBinding
import com.example.ecommerceapp.presentation.adapter.GetProductsAdapter
import com.example.ecommerceapp.presentation.viewMdoel.GetAllProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FashionFragment : Fragment() {

    private var _binding: FragmentFashionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GetAllProductsViewModel by viewModels()
    private lateinit var fashionAdapter: GetProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFashionBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fashionAdapter = GetProductsAdapter()
        setUpRecyclerViewForFashion()
        observeFashionProducts()
        viewModel.getProductsByCategory(category = "fashion")

        binding.imvBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        fashionAdapter.setOnClickListener {
            val action = FashionFragmentDirections
                .actionFashionFragmentToProductDetailsFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun observeFashionProducts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { productState ->
                    binding.loadingPrg.visibility =
                        if (productState.isLoading) View.VISIBLE else View.GONE

                    fashionAdapter.differ.submitList(productState.products)
                    Log.d("FashionFragment", "Categories: ${productState.products.map { it.category }}")

                    binding.txvEmptyState.visibility =
                        if(productState.products.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.event.collect { event ->
                when (event) {
                    is GetAllProductsViewModel.UiEvent.ToastMessage -> {
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
        binding.rcvFashion.apply {
            adapter = fashionAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}