package com.example.ecommerceapp.presentation.ui.fragment.mainScreen

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
import com.example.ecommerceapp.presentation.adapter.ProductsAdapter
import com.example.ecommerceapp.presentation.viewMdoel.RemoteProductsViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RemoteProductsViewModel by viewModels()
    private lateinit var auth: FirebaseAuth

    private lateinit var productsAdapter: ProductsAdapter

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
        auth = FirebaseAuth.getInstance()
        productsAdapter = ProductsAdapter()

        setUpProductsRecyclerView()
        getAvailableProducts()

        fetchProductsByItsCategory()
        setUserProfilePictureAndName()
        setImageSlider()

        productsAdapter.setOnClickListener { product ->
            val action = HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(product)
            findNavController().navigate(action)
        }

        binding.imvSearch.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            findNavController().navigate(action)
        }

        binding.txvViewAllProducts.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAllProductsFragment()
            )
        }
    }

    private fun getAvailableProducts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getSomeProducts.collectLatest { productsList ->
                    binding.loadingProgressBar.visibility =
                        if (productsList.isLoading) View.VISIBLE else View.GONE

                    if (productsList.products.isNotEmpty()) {
                        productsAdapter.differ.submitList(productsList.products)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when(event) {
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

    private fun setUpProductsRecyclerView() {
        binding.rcvHotDeals.apply {
            adapter = productsAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun fetchProductsByItsCategory() {
        binding.btnFashion.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToFashionFragment()
            findNavController().navigate(action)
        }

        binding.btnBeauty.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToBeautyFragment()
            findNavController().navigate(action)
        }

        binding.btnFurniture.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToFurnitureFragment()
            findNavController().navigate(action)
        }

        binding.btnFragrances.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToFragrancesFragment()
            findNavController().navigate(action)
        }

        binding.btnGroceries.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToGroceriesFragment()
            findNavController().navigate(action)
        }
    }

    private fun setUserProfilePictureAndName() {
        val user = auth.currentUser
        user?.let {
            binding.txvUserName.text = it.displayName ?: "Guest"

            if(it.photoUrl != null) {
                try {

                    binding.imvProfile.setImageURI(it.photoUrl)
                }  catch (e: SecurityException) {
                    binding.imvProfile.setImageResource(R.drawable.pp)
                }
            } else {
                binding.imvProfile.setImageResource(R.drawable.pp)
            }
        }
    }

    private fun setImageSlider() {
        val imageSlider: ImageSlider = requireActivity().findViewById(R.id.imageSlider)
        val imageList = ArrayList<SlideModel>()

        imageList.add(SlideModel(R.drawable.beauty1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.fragrances1, ScaleTypes.FIT))

        imageSlider.setImageList(imageList, ScaleTypes.FIT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}