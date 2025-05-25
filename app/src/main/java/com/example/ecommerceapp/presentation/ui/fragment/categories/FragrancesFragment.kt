package com.example.ecommerceapp.presentation.ui.fragment.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerceapp.core.NavOption
import com.example.ecommerceapp.databinding.FragmentFragrancesBinding
import com.example.ecommerceapp.presentation.adapter.PagingAdapter
import com.example.ecommerceapp.presentation.viewMdoel.remote.CategoryProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragrancesFragment : Fragment() {

    private var _binding: FragmentFragrancesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoryProductsViewModel by viewModels()
    private lateinit var fragrancesAdapter: PagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFragrancesBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragrancesAdapter = PagingAdapter()
        setUpRecyclerViewForFashion()
        observeFragrancesProducts()
        navigateToSearchFragment()

        viewModel.getProductsByCategory(categories = listOf("fragrances"))

        binding.imvBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        fragrancesAdapter.addLoadStateListener { loadState ->
            _binding?.let { binding ->
                binding.progressBarLoading.isVisible = loadState.source.refresh is LoadState.Loading
            }
        }


        fragrancesAdapter.setOnClickListener {
            val action = FragrancesFragmentDirections
                .actionFragrancesFragmentToProductDetailsFragment(it)
            findNavController().navigate(action, NavOption.navOptions)
        }
    }

    private fun observeFragrancesProducts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categoryPagingData.collectLatest {
                    fragrancesAdapter.submitData(it)
                }
            }
        }
    }

    private fun setUpRecyclerViewForFashion() {
        binding.rcvFragrances.apply {
            adapter = fragrancesAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun navigateToSearchFragment() {
        binding.btnSearchBox.setOnClickListener {
            val action = FragrancesFragmentDirections.actionFragrancesFragmentToSearchFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}