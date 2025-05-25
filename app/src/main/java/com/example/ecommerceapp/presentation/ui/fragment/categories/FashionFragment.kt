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
import com.example.ecommerceapp.databinding.FragmentFashionBinding
import com.example.ecommerceapp.presentation.adapter.PagingAdapter
import com.example.ecommerceapp.presentation.viewMdoel.remote.CategoryProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FashionFragment : Fragment() {

    private var _binding: FragmentFashionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoryProductsViewModel by viewModels()
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
        navigateToSearchFragment()

        val fashionCategories = listOf<String>(
            "mens-shirts", "mens-shoes", "tops",
            "womens-dresses", "womens-bags", "womens-shoes"
        )

        viewModel.getProductsByCategory(fashionCategories)

        binding.imvBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        fashionAdapter.addLoadStateListener { loadState ->
            _binding?.let { binding ->
                binding.progressBarLoading.isVisible = loadState.source.refresh is LoadState.Loading
            }
        }

        fashionAdapter.setOnClickListener {
            val action = FashionFragmentDirections
                .actionFashionFragmentToProductDetailsFragment(it)
            findNavController().navigate(action, NavOption.navOptions)
        }
    }

    private fun observeFashionProducts() {
        lifecycleScope.launch {
            viewModel.categoryPagingData.collectLatest {
                fashionAdapter.submitData(it)
            }
        }
    }

    private fun setUpRecyclerViewForFashion() {
        binding.rcvFashion.apply {
            adapter = fashionAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun navigateToSearchFragment() {
        binding.btnSearchBox.setOnClickListener {
            val action = FashionFragmentDirections.actionFashionFragmentToSearchFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}