package com.example.ecommerceapp.presentation.ui.fragment.mainScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentAccountBinding
import com.example.ecommerceapp.presentation.adapter.ProductsAdapter
import com.example.ecommerceapp.presentation.viewMdoel.local.LocalProductsViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

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

        setUserProfilePictureAndName()
        setLogout()

        binding.imvEdit.setOnClickListener {
            val action = AccountFragmentDirections
                .actionAccountFragmentToEditAccountFragment()
            findNavController().navigate(action)
        }
    }

    private fun setUserProfilePictureAndName() {
        val user = auth.currentUser
        user?.let {
            binding.txvName.text = it.displayName ?: "Unknown"
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

    private fun setLogout() {
        binding.btnLogout.setOnClickListener {
            auth.signOut()

            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToLoginFragment())
        }
    }

    private fun setAboutMe() {
        binding.btnAboutMe.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToAboutMeFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}