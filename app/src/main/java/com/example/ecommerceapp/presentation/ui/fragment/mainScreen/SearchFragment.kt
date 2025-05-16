package com.example.ecommerceapp.presentation.ui.fragment.mainScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerceapp.databinding.FragmentSearchBinding
import com.example.ecommerceapp.presentation.adapter.PagingAdapter
import com.example.ecommerceapp.presentation.viewMdoel.CartViewModel
import com.example.ecommerceapp.presentation.viewMdoel.SearchingProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagingAdapter: PagingAdapter
    private val viewModel: SearchingProductsViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagingAdapter = PagingAdapter()

        observeSearchProducts()
        setUpRecyclerViewForSearchProducts()

        pagingAdapter.setOnAddToCartClickListener {
            cartViewModel.addToCart(it)
            Toast.makeText(requireContext(), "Added to Cart", Toast.LENGTH_SHORT).show()
        }

        pagingAdapter.setOnClickListener {
            val action = SearchFragmentDirections
                .actionSearchFragmentToProductDetailsFragment(it)
            findNavController().navigate(action)
        }

    }

    private fun observeSearchProducts() {
        binding.edtSearchItem.addTextChangedListener { editable ->
            val query = editable.toString().trim()
            if (query.isNotEmpty()) {
                viewModel.searchingProducts(query)
            }
        }

        lifecycleScope.launch {
            viewModel.searchResult.collectLatest {
                pagingAdapter.submitData(it)
            }
        }
    }

    private fun setUpRecyclerViewForSearchProducts() {
        binding.rcvSearchItems.apply {
            adapter = pagingAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}