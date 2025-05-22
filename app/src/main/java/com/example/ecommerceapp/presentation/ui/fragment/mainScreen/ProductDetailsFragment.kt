package com.example.ecommerceapp.presentation.ui.fragment.mainScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ecommerceapp.R
import com.example.ecommerceapp.core.NavOption
import com.example.ecommerceapp.databinding.FragmentProductDetailsBinding
import com.example.ecommerceapp.domain.model.ReviewVo
import com.example.ecommerceapp.presentation.adapter.PagingAdapter
import com.example.ecommerceapp.presentation.viewMdoel.CartViewModel
import com.example.ecommerceapp.presentation.viewMdoel.LocalProductsViewModel
import com.example.ecommerceapp.presentation.viewMdoel.RemoteProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: ProductDetailsFragmentArgs by navArgs()
    private val viewModel: LocalProductsViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private val remoteViewModel: RemoteProductsViewModel by viewModels()
    private lateinit var pagingAdapter: PagingAdapter

    private var productWasSaved = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductDetailsBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagingAdapter = PagingAdapter()

        getProductDetails()
        observeSavedProduct()
        addToCart()

        fetchSimilarProducts()
        setSimilarProductsRecyclerView()

        binding.imvBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.imvFavorite.setOnClickListener {
            val isSaved = viewModel.isProductSaved(args.product)
            viewModel.toggleProductSave(args.product)

            if(!isSaved){
                productWasSaved = true
                Toast.makeText(requireContext(), "Product saved", Toast.LENGTH_SHORT).show()
            } else {
                productWasSaved = false
                Toast.makeText(requireContext(), "Product removed", Toast.LENGTH_SHORT).show()
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun getProductDetails() {
        val products = args.product
        view?.let {
            Glide.with(it)
                .load(products.thumbnail)
                .into(binding.imvProductImage)
        }

        binding.txvProductBrand.text = "Brand: " + products.brand
        binding.txvProductName.text = products.title
        binding.txvProductPrice.text = "$"+ products.price.toString()+" USD"
        binding.txvProduceDescription.text = products.description
        binding.txvProductRating.text = String.format("%.1f", products.rating)
        binding.txvStockStatus.text = products.availabilityStatus

        binding.txvReview.setOnClickListener {
            productReview()
        }
    }

    private fun productReview() {
        val review = args.product.reviews
        if(review!!.isNotEmpty()) {
            val bottomSheet = ReviewBottomSheet.Companion.newInstance(review as List<ReviewVo>)
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        } else {
            Toast.makeText(requireContext(), "No reviews available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeSavedProduct() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    updateFavoriteIcon()
                }
            }
        }
    }

    private fun updateFavoriteIcon(){
        val isSaved = viewModel.isProductSaved(args.product)
        val icon = if(isSaved) R.drawable.favorite2 else R.drawable.favorite1
        binding.imvFavorite.setImageResource(icon)
    }

    private fun addToCart() {
        binding.btnAddToCart.setOnClickListener {
            cartViewModel.addToCart(args.product)
            Toast.makeText(requireContext(), "Added To Cart", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchSimilarProducts() {
        args?.product?.category?.let { category ->
            remoteViewModel.getProductsByCategory(listOf(category))
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                remoteViewModel.categoryPagingData.collectLatest {
                    pagingAdapter.submitData(it)
                }
            }
        }

        pagingAdapter.setOnClickListener {
            findNavController().navigate(
                ProductDetailsFragmentDirections.actionProductDetailsFragmentSelf(it)
            )
        }
    }

    private fun setSimilarProductsRecyclerView() {
        binding.rcvSimilarProducts.apply {
            adapter = pagingAdapter
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}