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
import com.example.ecommerceapp.databinding.FragmentFashionBinding
import com.example.ecommerceapp.presentation.adapter.PagingAdapter
import com.example.ecommerceapp.presentation.viewMdoel.RemoteProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FashionFragment : Fragment() {

    private var _binding: FragmentFashionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RemoteProductsViewModel by viewModels()
    private lateinit var fashionAdapter: PagingAdapter

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
        fashionAdapter = PagingAdapter()
        setUpRecyclerViewForFashion()
        observeFashionProducts()

        val fashionCategories = listOf<String>(
            "mens-shirts", "mens-shoes", "tops",
            "womens-dresses", "womens-bags", "womens-shoes"
        )

        viewModel.getProductsByCategory(fashionCategories)

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
        lifecycleScope.launch {
            viewModel.categoryPagingData.collectLatest {
                fashionAdapter.addLoadStateListener { loadState ->
                    binding.progressBarLoading.isVisible = loadState.source.refresh is LoadState.Loading
                }
                fashionAdapter.submitData(it)
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