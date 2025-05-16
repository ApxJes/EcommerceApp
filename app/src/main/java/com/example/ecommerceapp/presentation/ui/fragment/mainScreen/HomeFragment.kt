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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentHomeBinding
import com.example.ecommerceapp.domain.model.Product
import com.example.ecommerceapp.presentation.adapter.PagingAdapter
import com.example.ecommerceapp.presentation.viewMdoel.CartViewModel
import com.example.ecommerceapp.presentation.viewMdoel.GetAllProductsViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GetAllProductsViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var pagingAdapter: PagingAdapter

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
        pagingAdapter = PagingAdapter()
        setUpRecyclerViewForProducts()

        getProducts()
        discountProducts()
        getProductsByCategory()
        setUserProfilePictureAndName()
        addToCart()
        searchingProducts()

        pagingAdapter.setOnClickListener { product ->
            val action = HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(product)
            findNavController().navigate(action)
        }
    }

    private fun getProducts() {

        lifecycleScope.launch {
            viewModel.pagedProduct.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when(event) {
                        is GetAllProductsViewModel.UiEvent.ToastMessage -> {
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
            adapter = pagingAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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

    private fun getProductsByCategory() {
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

    private fun addToCart() {
        pagingAdapter.setOnAddToCartClickListener {
            cartViewModel.addToCart(it)
            Toast.makeText(requireContext(), "Added to Cart", Toast.LENGTH_SHORT).show()
        }
    }

    private fun searchingProducts() {
        binding.edtSearchItem.addTextChangedListener { editable ->
            val query = editable.toString().trim()
            if(query.isNotEmpty()) {
                viewModel.searchingProducts(query)
            }
        }

        lifecycleScope.launch {
            viewModel.searchResult.collectLatest {
                pagingAdapter.submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}