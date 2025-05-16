package com.example.ecommerceapp.presentation.ui.fragment.mainScreen

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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentAccountBinding
import com.example.ecommerceapp.presentation.adapter.GetProductsAdapter
import com.example.ecommerceapp.presentation.viewMdoel.CartViewModel
import com.example.ecommerceapp.presentation.viewMdoel.GetProductsViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var productAdapter: GetProductsAdapter
    private val viewModel: GetProductsViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
        requireActivity().contentResolver.persistedUriPermissions
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        productAdapter = GetProductsAdapter()

        observeSaveProducts()
        setUpRecyclerViewForSaveProducts()
        setUserProfilePictureAndName()
        addToCart()

        binding.imvBack.setOnClickListener {
            findNavController().navigate(
                R.id.homeFragment, null, NavOptions.Builder()
                    .setPopUpTo(R.id.onBoardingFragment, true)
                    .build()
            )
        }

        binding.imvEdit.setOnClickListener {
            val action = AccountFragmentDirections
                .actionAccountFragmentToEditAccountFragment()
            findNavController().navigate(action)
        }

        productAdapter.setOnClickListener { product ->
            val action = AccountFragmentDirections
                .actionAccountFragmentToProductDetailsFragment(product)
            findNavController().navigate(action)
        }
    }

    private fun setUserProfilePictureAndName() {
        val user = auth.currentUser
        user?.let {
            binding.txvName.text = it.displayName ?: "Guest"
            binding.txvEmail.text = it.email

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

    private fun observeSaveProducts(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest {saveProducts ->
                    productAdapter.differ.submitList(saveProducts)
                }
            }
        }
    }

    private fun setUpRecyclerViewForSaveProducts(){
        binding.rcvSaveProducts.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun addToCart() {
        productAdapter.setOnAddToCartClickListener {
            cartViewModel.addToCart(it)
            Toast.makeText(requireContext(), "Added to Cart", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}