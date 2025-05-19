package com.example.ecommerceapp.presentation.ui.fragment.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerceapp.databinding.FragmentBeautyBinding
import com.example.ecommerceapp.presentation.adapter.PagingAdapter
import com.example.ecommerceapp.presentation.viewMdoel.RemoteProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class BeautyFragment : Fragment() {
    private var _binding: FragmentBeautyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RemoteProductsViewModel by viewModels()
    private lateinit var beautyAdapter: PagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBeautyBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beautyAdapter = PagingAdapter()
        setUpRecyclerViewForFashion()
        observeBeautyProducts()
        viewModel.getProductsByCategory(category = "beauty")

        binding.imvBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        beautyAdapter.setOnClickListener {
            val action = BeautyFragmentDirections
                .actionBeautyFragmentToProductDetailsFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun observeBeautyProducts() {

        lifecycleScope.launch {
            viewModel.categoryPagingData.collectLatest {
                beautyAdapter.submitData(it)
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

    private fun setUpRecyclerViewForFashion() {
        binding.rcvBeauty.apply {
            adapter = beautyAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}