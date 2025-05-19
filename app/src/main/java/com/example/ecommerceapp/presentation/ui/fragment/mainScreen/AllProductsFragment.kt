package com.example.ecommerceapp.presentation.ui.fragment.mainScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentAllProductsBinding
import com.example.ecommerceapp.presentation.adapter.PagingAdapter
import com.example.ecommerceapp.presentation.viewMdoel.RemoteProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllProductsFragment : Fragment() {

    private var _binding: FragmentAllProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagingAdapter: PagingAdapter
    private val viewModel: RemoteProductsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllProductsBinding.inflate(
            inflater,
            container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagingAdapter = PagingAdapter()

        fetchAllProducts()
        setUpAllProductsRecyclerView()

        pagingAdapter.setOnClickListener {
            findNavController().navigate(
                AllProductsFragmentDirections.actionAllProductsFragmentToProductDetailsFragment(it)
            )
        }

        binding.imvBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun fetchAllProducts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getAllAvailableProducts.collectLatest {
                    pagingAdapter.submitData(it)
                }
            }
        }
    }

    private fun setUpAllProductsRecyclerView() {
        binding.rcvAllProducts.apply {
            adapter = pagingAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}