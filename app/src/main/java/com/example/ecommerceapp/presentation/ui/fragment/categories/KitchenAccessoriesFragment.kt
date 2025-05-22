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
import com.example.ecommerceapp.databinding.FragmentKitchenAccessoriesBinding
import com.example.ecommerceapp.presentation.adapter.PagingAdapter
import com.example.ecommerceapp.presentation.ui.fragment.mainScreen.HomeFragmentDirections
import com.example.ecommerceapp.presentation.viewMdoel.RemoteProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class KitchenAccessoriesFragment : Fragment() {
    private var _binding: FragmentKitchenAccessoriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagingAdapter: PagingAdapter
    private val viewModel: RemoteProductsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKitchenAccessoriesBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagingAdapter = PagingAdapter()

        setKitchenAccessoriesRecyclerView()
        fetchKitchenAccessory()
        navigateToSearchFragment()

        binding.imvBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        pagingAdapter.setOnClickListener {
            findNavController().navigate(
                KitchenAccessoriesFragmentDirections.actionKitchenAccessoriesFragmentToProductDetailsFragment(it)
            )
        }
    }

    private fun fetchKitchenAccessory() {
        viewModel.getProductsByCategory(category = listOf("kitchen-accessories"))

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

    private fun setKitchenAccessoriesRecyclerView() {
        binding.rcvKitchenAccessory.apply {
            adapter = pagingAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun navigateToSearchFragment() {
        binding.btnSearchBox.setOnClickListener {
            val action = KitchenAccessoriesFragmentDirections.actionKitchenAccessoriesFragmentToSearchFragment()
            findNavController().navigate(action, NavOption.navOptions)
        }

        binding.tvSearch.setOnClickListener {
            val action = KitchenAccessoriesFragmentDirections.actionKitchenAccessoriesFragmentToSearchFragment()
            findNavController().navigate(action, NavOption.navOptions)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}