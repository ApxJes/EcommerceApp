package com.example.ecommerceapp.presentation.ui.fragment.mainScreen

import android.annotation.SuppressLint
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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentProductDetailsBinding
import com.example.ecommerceapp.domain.model.Review
import com.example.ecommerceapp.presentation.viewMdoel.GetProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: ProductDetailsFragmentArgs by navArgs()
    private val viewModel: GetProductsViewModel by viewModels()

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
        getProductDetails()
        observeSavedProduct()

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
        binding.txvProductPrice.text = "$"+ products.price.toString()
        binding.txvProduceDescription.text = products.description
        binding.txvProductRating.text = String.format("%.1f", products.rating)

        binding.txvReview.setOnClickListener {
            productReview()
        }
    }

    private fun productReview() {

        val review = args.product.reviews
        if(review!!.isNotEmpty()) {
            val bottomSheet = ReviewBottomSheet.Companion.newInstance(review as List<Review>)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}