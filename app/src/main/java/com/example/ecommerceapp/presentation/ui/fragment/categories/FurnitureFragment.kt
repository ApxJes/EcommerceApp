package com.example.ecommerceapp.presentation.ui.fragment.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerceapp.databinding.FragmentFurnitureBinding
import com.example.ecommerceapp.presentation.adapter.PagingAdapter
import com.example.ecommerceapp.presentation.viewMdoel.RemoteProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FurnitureFragment : Fragment() {

    private var _binding: FragmentFurnitureBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RemoteProductsViewModel by viewModels()
    private lateinit var furnitureAdapter: PagingAdapter

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
        furnitureAdapter = PagingAdapter()
        setUpRecyclerViewForFashion()
        observeFurnitureProducts()

        val homeDecorationCategories = listOf<String>(
            "furniture", "home-decoration"
        )

        viewModel.getProductsByCategory(homeDecorationCategories)

        binding.imvBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        furnitureAdapter.setOnClickListener {
            val action = FurnitureFragmentDirections
                .actionFurnitureFragmentToProductDetailsFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun observeFurnitureProducts() {

        lifecycleScope.launch {
            viewModel.categoryPagingData.collectLatest {
                furnitureAdapter.addLoadStateListener { loadState ->
                    binding.progressBarLoading.isVisible = loadState.source.refresh is LoadState.Loading
                }
                furnitureAdapter.submitData(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.event.collect { event ->
                when (event) {
                    is RemoteProductsViewModel.UiEvent.ToastMessage -> {
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