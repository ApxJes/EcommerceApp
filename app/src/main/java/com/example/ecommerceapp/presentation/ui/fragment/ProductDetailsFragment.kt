package com.example.ecommerceapp.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.ecommerceapp.databinding.FragmentProductDetailsBinding
import com.example.ecommerceapp.domain.model.Review
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: ProductDetailsFragmentArgs by navArgs()

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

        binding.imvBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

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
            val bottomSheet = ReviewBottomSheet.newInstance(review as List<Review>)
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        } else {
            Toast.makeText(requireContext(), "No reviews available", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}