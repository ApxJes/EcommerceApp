package com.example.ecommerceapp.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentHomeBinding
import com.example.ecommerceapp.domain.model.Product
import com.example.ecommerceapp.presentation.adapter.GetProductsAdapter
import com.example.ecommerceapp.presentation.viewMdoel.GetProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdpater: GetProductsAdapter
    private val viewModel: GetProductsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productAdpater = GetProductsAdapter()
        setUpRecyclerViewForProducts()

        getProducts()
        discountProducts()
        categories()

        productAdpater.setOnClickListener { product ->
            val action = HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(product)
            findNavController().navigate(action)
        }
    }

    private fun getProducts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { products ->
                    binding.loadingProgressBar.visibility =
                        if (products.isLoading) View.VISIBLE else View.GONE

                    if(products.products.isNotEmpty()) {
                         productAdpater.differ.submitList(products.products)
                    }

                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when(event) {
                        is GetProductsViewModel.UiEvent.ToastMessage -> {
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

    private fun setUpRecyclerViewForProducts() {
        binding.rcvHotDeals.apply {
            adapter = productAdpater
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun discountProducts() {
        val imageSlider = requireActivity().findViewById<ImageSlider>(R.id.imageSlider)
        val imageList = ArrayList<SlideModel>()

        imageList.add(SlideModel(R.drawable.fashion1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.fitness1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.beauty1, ScaleTypes.FIT))

        imageSlider.setImageList(imageList, ScaleTypes.FIT)
    }

    private fun categories() {
        fashionCategory()
    }

    private fun fashionCategory() {
        binding.btnFashion.setOnClickListener {
            val action = HomeFragmentDirections
                .actionHomeFragmentToFashionFragment()

            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}