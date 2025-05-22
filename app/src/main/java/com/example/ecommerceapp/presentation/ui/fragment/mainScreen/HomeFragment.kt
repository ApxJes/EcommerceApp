package com.example.ecommerceapp.presentation.ui.fragment.mainScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.ecommerceapp.R
import com.example.ecommerceapp.core.NavOption
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
        toSeeAllAvailableProducts()
        toSeeAllCategories()
        navigateToSearchFragment()

        productsAdapter.setOnClickListener { product ->
            val action = HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(product)
            findNavController().navigate(action, NavOption.navOptions)
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

        val navOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.fade_in)
            .setExitAnim(R.anim.fade_out)
            .setPopEnterAnim(R.anim.fade_in)
            .setPopExitAnim(R.anim.fade_out)
            .build()

        binding.btnFashion.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToFashionFragment()
            findNavController().navigate(action, navOptions)
        }

        binding.btnBeauty.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToBeautyFragment()
            findNavController().navigate(action, navOptions)
        }

        binding.btnFurniture.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToFurnitureFragment()
            findNavController().navigate(action, navOptions)
        }

        binding.btnFragrances.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToFragrancesFragment()
            findNavController().navigate(action, navOptions)
        }

        binding.btnGroceries.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToGroceriesFragment()
            findNavController().navigate(action, navOptions)
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

        imageList.add(SlideModel(R.drawable.recommend_poster, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.popular_poster, ScaleTypes.FIT))
        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                when(position) {
                    0 -> findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToRecommendProductsFragment())
                    1 -> findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPopularProductsFragment())
                }
            }

        })
    }

    private fun toSeeAllAvailableProducts() {
        binding.txvViewAllProducts.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAllProductsFragment()
            )
        }

        binding.txvViewAllProducts.isClickable = true
        binding.txvViewAllProducts.isFocusable = false
    }

    private fun toSeeAllCategories() {
        binding.txvViewAllCategories.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAllCategoriesFragment()
            )
        }

        binding.txvViewAllCategories.isClickable = true
        binding.txvViewAllCategories.isFocusable = false
    }

    private fun navigateToSearchFragment() {
        binding.btnSearchBox.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            findNavController().navigate(action)
        }

        binding.tvSearch.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}