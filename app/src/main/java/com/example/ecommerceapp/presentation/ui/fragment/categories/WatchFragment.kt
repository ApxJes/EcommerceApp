package com.example.ecommerceapp.presentation.ui.fragment.categories

import android.os.Bundle
import android.util.Log
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
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerceapp.core.NavOption
import com.example.ecommerceapp.databinding.FragmentWatchBinding
import com.example.ecommerceapp.domain.model.ProductVo
import com.example.ecommerceapp.presentation.adapter.PagingAdapter
import com.example.ecommerceapp.presentation.viewMdoel.remote.CategoryProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WatchFragment : Fragment() {
    private var _binding: FragmentWatchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoryProductsViewModel by viewModels()
    private lateinit var pagingAdapter: PagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWatchBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagingAdapter = PagingAdapter()

        setWatchRecyclerView()
        navigateToSearchFragment()

        binding.imvBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        pagingAdapter.addLoadStateListener { loadState ->
            _binding?.let { binding ->
                binding.progressBarLoading.isVisible = loadState.source.refresh is LoadState.Loading
            }
        }

        pagingAdapter.setOnClickListener {
            findNavController().navigate(
                WatchFragmentDirections.actionWatchFragmentToProductDetailsFragment(it)
            )
        }

        fetchWatches()
    }

    private fun fetchWatches() {
        viewModel.getProductsByCategory(categories = listOf("womens-watches", "mens-watches"))

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categoryPagingData.collectLatest { pagingData ->
                    pagingAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun setWatchRecyclerView() {
        binding.rcvWatch.apply {
            adapter = pagingAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun navigateToSearchFragment() {
        binding.btnSearchBox.setOnClickListener {
            val action = WatchFragmentDirections.actionWatchFragmentToSearchFragment()
            findNavController().navigate(action, NavOption.navOptions)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}