package com.example.ecommerceapp.presentation.ui.fragment.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerceapp.core.NavOption
import com.example.ecommerceapp.databinding.FragmentMobileAccessoryBinding
import com.example.ecommerceapp.presentation.adapter.PagingAdapter
import com.example.ecommerceapp.presentation.ui.fragment.mainScreen.HomeFragmentDirections
import com.example.ecommerceapp.presentation.viewMdoel.RemoteProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MobileAccessoryFragment : Fragment() {
    private var _binding: FragmentMobileAccessoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RemoteProductsViewModel by viewModels()
    private lateinit var pagingAdapter: PagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMobileAccessoryBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagingAdapter = PagingAdapter()

        setMobileAccessoriesRecyclerView()
        fetchMobileAccessories()
        navigateToSearchFragment()

        binding.imvBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        pagingAdapter.setOnClickListener {
            findNavController().navigate(
                MobileAccessoryFragmentDirections.actionMobileAccessoryFragmentToProductDetailsFragment(it)
            )
        }
    }

    private fun fetchMobileAccessories() {
        viewModel.getProductsByCategory(category = listOf("mobile-accessories"))

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categoryPagingData.collectLatest {
                    pagingAdapter.addLoadStateListener { loadState ->
                        binding.progressBarLoading.isVisible = loadState.source.refresh is LoadState.Loading
                    }
                    pagingAdapter.submitData(it)
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
    }

    private fun setMobileAccessoriesRecyclerView() {
        binding.rcvMobileAccessory.apply {
            adapter = pagingAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun navigateToSearchFragment() {
        binding.btnSearchBox.setOnClickListener {
            val action = MobileAccessoryFragmentDirections.actionMobileAccessoryFragmentToSearchFragment()
            findNavController().navigate(action, NavOption.navOptions)
        }

        binding.tvSearch.setOnClickListener {
            val action = MobileAccessoryFragmentDirections.actionMobileAccessoryFragmentToSearchFragment()
            findNavController().navigate(action, NavOption.navOptions)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}