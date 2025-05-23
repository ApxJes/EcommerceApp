package com.example.ecommerceapp.presentation.ui.fragment.mainScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentSaveBinding
import com.example.ecommerceapp.presentation.adapter.ProductsAdapter
import com.example.ecommerceapp.presentation.viewMdoel.LocalProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class SaveFragment : Fragment() {

    private var _binding: FragmentSaveBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductsAdapter
    private val viewModel: LocalProductsViewModel by viewModels()

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

        productAdapter = ProductsAdapter()

        observeSaveProducts()
        setUpRecyclerViewForSaveProducts()

        productAdapter.setOnClickListener { product ->
            val action = SaveFragmentDirections
                .actionSaveFragmentToProductDetailsFragment(product)
            findNavController().navigate(action)
        }
    }

    private fun observeSaveProducts(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { saveProducts ->
                    productAdapter.differ.submitList(saveProducts)

                    if(saveProducts.isEmpty()) {
                        binding.emptyLayout.visibility = View.VISIBLE
                        binding.rcvSave.visibility = View.GONE
                    } else {
                        binding.emptyLayout.visibility = View.GONE
                        binding.rcvSave.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setUpRecyclerViewForSaveProducts(){
        binding.rcvSave.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}