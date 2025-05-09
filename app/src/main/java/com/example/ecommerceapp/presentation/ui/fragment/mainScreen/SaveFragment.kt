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
import com.example.ecommerceapp.databinding.FragmentSaveBinding
import com.example.ecommerceapp.presentation.adapter.GetProductsAdapter
import com.example.ecommerceapp.presentation.viewMdoel.GetProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SaveFragment : Fragment() {

    private var _binding: FragmentSaveBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GetProductsViewModel by viewModels()
    private lateinit var productsAdapter: GetProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSaveBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productsAdapter = GetProductsAdapter()

        observeSaveProducts()
        setUpRecyclerViewForSaveProducts()

        productsAdapter.setOnClickListener { product ->
            val action = SaveFragmentDirections
                .actionSaveFragmentToProductDetailsFragment(product)
            findNavController().navigate(action)
        }

        binding.imvBack.setOnClickListener {
            findNavController().navigate(
                R.id.homeFragment, null, NavOptions.Builder()
                    .setPopUpTo(R.id.onBoardingFragment, true)
                    .build()
            )
        }
    }

    private fun observeSaveProducts(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest {saveProducts ->
                    productsAdapter.differ.submitList(saveProducts)
                }
            }
        }
    }

    private fun setUpRecyclerViewForSaveProducts(){
        binding.rcvSaveProducts.apply {
            adapter = productsAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}