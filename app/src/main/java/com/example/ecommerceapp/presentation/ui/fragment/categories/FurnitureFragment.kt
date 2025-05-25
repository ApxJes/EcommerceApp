package com.example.ecommerceapp.presentation.ui.fragment.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerceapp.core.NavOption
import com.example.ecommerceapp.databinding.FragmentFurnitureBinding
import com.example.ecommerceapp.presentation.adapter.PagingAdapter
import com.example.ecommerceapp.presentation.viewMdoel.remote.CategoryProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FurnitureFragment : Fragment() {

    private var _binding: FragmentFurnitureBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoryProductsViewModel by viewModels()
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
        navigateToSearchFragment()

        val homeDecorationCategories = listOf<String>(
            "furniture", "home-decoration"
        )

        viewModel.getProductsByCategory(homeDecorationCategories)

        binding.imvBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        furnitureAdapter.addLoadStateListener { loadState ->
            _binding?.let { binding ->
                binding.progressBarLoading.isVisible = loadState.source.refresh is LoadState.Loading
            }
        }

        furnitureAdapter.setOnClickListener {
            val action = FurnitureFragmentDirections
                .actionFurnitureFragmentToProductDetailsFragment(it)
            findNavController().navigate(action, NavOption.navOptions)
        }
    }

    private fun observeFurnitureProducts() {

        lifecycleScope.launch {
            viewModel.categoryPagingData.collectLatest {
                furnitureAdapter.submitData(it)
            }
        }
    }

    private fun setUpRecyclerViewForFashion() {
        binding.rcvFurniture.apply {
            adapter = furnitureAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun navigateToSearchFragment() {
        binding.btnSearchBox.setOnClickListener {
            val action = FurnitureFragmentDirections.actionFurnitureFragmentToSearchFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}